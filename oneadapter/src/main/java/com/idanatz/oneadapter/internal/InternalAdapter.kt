package com.idanatz.oneadapter.internal

import android.os.Handler
import android.os.HandlerThread
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ListUpdateCallback
import com.idanatz.oneadapter.external.interfaces.*
import com.idanatz.oneadapter.internal.diffing.OneDiffUtil
import com.idanatz.oneadapter.internal.holders.EmptyIndicator
import com.idanatz.oneadapter.internal.holders.LoadingIndicator
import com.idanatz.oneadapter.internal.holders.OneViewHolder
import com.idanatz.oneadapter.internal.interfaces.DiffUtilCallback
import com.idanatz.oneadapter.internal.interfaces.ViewHolderCreator
import com.idanatz.oneadapter.internal.paging.LoadMoreObserver
import com.idanatz.oneadapter.internal.paging.EndlessScrollListener
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.oneadapter.internal.selection.OneItemSelection
import com.idanatz.oneadapter.internal.selection.ItemSelectionActionsProvider
import com.idanatz.oneadapter.internal.selection.OneItemDetailLookup
import com.idanatz.oneadapter.internal.selection.OneItemKeyProvider
import com.idanatz.oneadapter.internal.selection.SelectionTrackerObserver
import com.idanatz.oneadapter.internal.selection.SelectionObserver
import com.idanatz.oneadapter.internal.swiping.OneItemTouchHelper
import com.idanatz.oneadapter.internal.utils.*
import com.idanatz.oneadapter.internal.utils.MissingBuilderArgumentException
import com.idanatz.oneadapter.internal.utils.MultipleHolderConflictException
import com.idanatz.oneadapter.internal.utils.let2
import com.idanatz.oneadapter.internal.utils.removeClassIfExist
import java.util.*

@Suppress("UNCHECKED_CAST", "NAME_SHADOWING")
internal class InternalAdapter : RecyclerView.Adapter<OneViewHolder<Any>>(), LoadMoreObserver, SelectionObserver, ItemSelectionActionsProvider {

    companion object {
        const val UPDATE_DATA_DELAY_MILLIS = 100L
    }

    private var recyclerView: RecyclerView? = null
    internal val modules = Modules()
    internal var data: MutableList<Any> = mutableListOf()
        private set

    // Items
    private val dataTypes = mutableListOf<Class<*>>() // maps T.class to unique indexes for adapter's getItemViewType
    private val holderCreators = HashMap<Class<*>, ViewHolderCreator<Any>>() // maps T.class -> ViewHolderCreator<T>

    // Paging
    private var pagingCreator: ViewHolderCreator<Any>? = null
    private var endlessScrollListener: EndlessScrollListener? = null

    // Emptiness
    private var emptinessCreator: ViewHolderCreator<Any>? = null

    // Item Selection
    private var selectionTracker: SelectionTracker<Long>? = null
    private var itemKeyProvider: OneItemKeyProvider? = null

    // Handlers
    private val backgroundHandler = Handler(HandlerThread("OneAdapterBackgroundHandler").apply { start() }.looper)
    private val uiHandler
        get() = recyclerView?.handler

    private val diffCallback = object : DiffUtilCallback {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.getUniqueIdentifier() == newItem.getUniqueIdentifier()
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.areContentTheSame(newItem)
    }

    init {
        setHasStableIds(true)
    }

    //region Traditional Adapter Overrides
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            when (viewType) {
                EmptyIndicator.getType() -> emptinessCreator?.create(parent) ?: throw IllegalArgumentException("Empty state has no associated Module attached")
                LoadingIndicator.getType() -> pagingCreator?.create(parent) ?: throw IllegalArgumentException("Loading state has no associated Module attached")
                else -> {
                    val dataType = dataTypes[viewType]
                    holderCreators[dataType]?.create(parent) ?: throw IllegalArgumentException("$dataType has no associated Module attached")
                }
            }

    override fun onBindViewHolder(holder: OneViewHolder<Any>, position: Int) {
        val model =
            when (val model = data[position]) {
                is EmptyIndicator, is LoadingIndicator -> null
                else -> model
            }

        Logger.logd {"onBindViewHolder -> position: $position, model: $model" }
        holder.onBindViewHolder(model)
    }

    private fun onBindSelection(holder: OneViewHolder<Any>, position: Int, selected: Boolean) {
        val model =
            when (val model = data[position]) {
                is EmptyIndicator, is LoadingIndicator -> null
                else -> model
            }
        Logger.logd {"onBindSelection -> position: $position, model: $model, selected: $selected" }
        holder.onBindSelection(model, selected)
    }

    override fun getItemCount() = data.size

    override fun getItemId(position: Int): Long = when(val item = data[position]) {
        // javaClass is used for lettings different Diffable models share the same unique identifier
        is Diffable -> item.javaClass.simpleName.hashCode() + item.getUniqueIdentifier()
        else -> item.hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int = when (val item = data[position]) {
        is EmptyIndicator -> EmptyIndicator.getType()
        is LoadingIndicator -> LoadingIndicator.getType()
        else -> {
            if (dataTypes.indexOf(item.javaClass) == -1) {
                dataTypes.add(item.javaClass)
            }
            dataTypes.indexOf(item.javaClass)
        }
    }

    override fun onViewDetachedFromWindow(holder: OneViewHolder<Any>) {
        super.onViewDetachedFromWindow(holder)
        holder.onUnbind()
    }
    //endregion

    fun updateData(data: MutableList<Any>) {
        backgroundHandler.post {
            // modify the incoming data if needed
            if (data.isEmpty()) {
                if (emptinessCreator != null) {
                    data.add(0, EmptyIndicator)
                }
                endlessScrollListener?.resetState()
            } else {
                data.removeClassIfExist(EmptyIndicator::class.java)
                data.removeClassIfExist(LoadingIndicator::class.java)
            }

            // handle the diffing
            val diffResult = DiffUtil.calculateDiff(OneDiffUtil(this.data, data, diffCallback))
            this.data = data
            uiHandler?.post {
                diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
                    override fun onInserted(position: Int, count: Int) {
                        Logger.logd { "onInserted -> position: $position, count: $count" }
                        notifyItemRangeInserted(position, count)
                    }
                    override fun onRemoved(position: Int, count: Int) {
                        Logger.logd { "onRemoved -> position: $position, count: $count" }
                        notifyItemRangeRemoved(position, count)
                    }
                    override fun onMoved(fromPosition: Int, toPosition: Int) {
                        Logger.logd { "onRemoved -> fromPosition: $fromPosition, toPosition: $toPosition" }
                        notifyItemMoved(fromPosition, toPosition)
                    }
                    override fun onChanged(position: Int, count: Int, payload: Any?) {
                        Logger.logd { "onChanged -> position: $position, count: $count, payload: $payload" }
                        notifyItemRangeChanged(position, count, payload)
                    }
                })
            }
        }
    }

    //region Item Module
    fun <M : Any> register(itemModule: ItemModule<M>) {
        val holderModuleConfig = itemModule.provideModuleConfig()
        val dataClass = extractGenericClass(itemModule.javaClass) ?: throw MissingBuilderArgumentException("ItemModuleConfig missing model class")

        if (holderCreators.containsKey(dataClass)) {
            throw MultipleHolderConflictException("ItemModule with model class ${dataClass.simpleName} already attached")
        }

        holderCreators[dataClass] = object : ViewHolderCreator<M> {
            override fun create(parent: ViewGroup): OneViewHolder<M> {
                return object : OneViewHolder<M>(parent, holderModuleConfig, itemModule.statesMap, itemModule.eventHooksMap) {
                    override fun onBind(model: M?) { model?.let { itemModule.onBind(it, viewBinder) } }
                    override fun onUnbind() = itemModule.onUnbind(viewBinder)
                }
            }
        } as ViewHolderCreator<Any>
    }
    //endregion

    //region Emptiness Module
    fun enableEmptiness(emptinessModule: EmptinessModule) {
        modules.emptinessModule = emptinessModule
        emptinessCreator = object : ViewHolderCreator<Any> {
            override fun create(parent: ViewGroup): OneViewHolder<Any> {
                return object : OneViewHolder<Any>(parent, emptinessModule.provideModuleConfig()) {
                    override fun onBind(model: Any?) = emptinessModule.onBind(viewBinder)
                    override fun onUnbind() = emptinessModule.onUnbind(viewBinder)
                }
            }
        }
    }
    //endregion

    //region Paging Module
    fun enablePaging(pagingModule: PagingModule) {
        // save the module and the config for later use, e.g invoking onLoadMore callback
        pagingModule.pagingModuleConfig = pagingModule.provideModuleConfig()
        modules.pagingModule = pagingModule

        pagingCreator = object : ViewHolderCreator<Any> {
            override fun create(parent: ViewGroup): OneViewHolder<Any> {
                return object : OneViewHolder<Any>(parent, pagingModule.pagingModuleConfig) {
                    override fun onBind(model: Any?) = pagingModule.onBind(viewBinder)
                    override fun onUnbind() = pagingModule.onUnbind(viewBinder)
                }
            }
        }
    }

    override fun onLoadingStateChanged(loading: Boolean) {
        if (loading) {
            data.indexOfFirst { it is LoadingIndicator }.let { index ->
                if (index == -1) {
                    data.add(data.size, LoadingIndicator)

                    // post it to the UI handler because the recycler crashes when calling notify from an onScroll callback
                    uiHandler?.post { notifyItemInserted(data.size) }
                }
            }
        }
    }

    override fun onLoadMore(currentPage: Int) {
        modules.pagingModule?.onLoadMore(currentPage)
    }
    //endregion

    //region Selection Module
    fun enableSelection(itemSelectionModule: ItemSelectionModule) {
        modules.oneItemSelection = OneItemSelection(itemSelectionModule, ItemSelectionActions(this))
    }

    override fun onItemStateChanged(key: Long, selected: Boolean) {
        let2(recyclerView, itemKeyProvider) { recyclerView, itemKeyProvider ->
            recyclerView.findViewHolderForItemId(key)?.let { holder ->
                onBindSelection(holder as OneViewHolder<Any>, itemKeyProvider.getPosition(key), selected)
            }
        }
    }

    override fun onSelectionStateChanged() {
        selectionTracker?.let {
            modules.oneItemSelection?.module?.onSelectionUpdated(it.selection.size())
        }
    }

    override fun getSelectedItems(): List<Any> {
        return let2(selectionTracker, itemKeyProvider) { selectionTracker, itemKeyProvider ->
            selectionTracker.selection?.map { key -> itemKeyProvider.getPosition(key) }?.filter { it >= 0 }?.map { position -> data[position] } ?: emptyList()
        } ?: emptyList()
    }

    override fun removeSelectedItems() {
        val dataCopy = LinkedList(data).apply { removeAllItems(this, getSelectedItems()) }
        updateData(dataCopy)

        uiHandler?.postDelayed({
            clearSelection()
        }, UPDATE_DATA_DELAY_MILLIS)
    }

    override fun clearSelection() = selectionTracker?.clearSelection()
    //endregion

    //region RecyclerView
    fun attachTo(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView.apply {
            adapter = this@InternalAdapter
            OneItemTouchHelper().attachToRecyclerView(this)
            configureEmptinessModule()
            configurePagingModule(this)
            configureItemSelectionModule(this)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        endlessScrollListener?.let { recyclerView.removeOnScrollListener(it) }
    }
    //endregion

    //region Internal Configurations
    private fun configureEmptinessModule() {
        // in case emptiness module is configured, add empty indicator item
        if (data.isEmpty() && emptinessCreator != null) {
            data.add(0, EmptyIndicator)
        }
    }

    private fun configurePagingModule(recyclerView: RecyclerView) {
        let2(recyclerView.layoutManager, modules.pagingModule) { layoutManager, loadMoreModule ->
            endlessScrollListener = EndlessScrollListener(
                    layoutManager = layoutManager,
                    visibleThreshold = loadMoreModule.pagingModuleConfig.withVisibleThreshold(),
                    includeEmptyState = emptinessCreator != null,
                    loadMoreObserver = this@InternalAdapter
            ).also { recyclerView.addOnScrollListener(it) }
        }
    }

    private fun configureItemSelectionModule(recyclerView: RecyclerView) {
        modules.oneItemSelection?.module?.let { selectionModule ->
            selectionTracker = SelectionTracker.Builder(
                    recyclerView.id.toString(),
                    recyclerView,
                    OneItemKeyProvider(recyclerView).also { itemKeyProvider = it },
                    OneItemDetailLookup(recyclerView),
                    StorageStrategy.createLongStorage()
            )
            .withSelectionPredicate(when (selectionModule.provideModuleConfig().withSelectionType()) {
                ItemSelectionModuleConfig.SelectionType.Single -> SelectionPredicates.createSelectSingleAnything()
                ItemSelectionModuleConfig.SelectionType.Multiple -> SelectionPredicates.createSelectAnything()
            })
            .build()
            .also { it.addObserver(SelectionTrackerObserver(this@InternalAdapter)) }
        }
    }
    //endregion
}