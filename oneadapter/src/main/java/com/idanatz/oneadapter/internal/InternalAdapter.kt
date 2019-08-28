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
import com.idanatz.oneadapter.internal.selection.ItemSelectionActionsProvider
import com.idanatz.oneadapter.internal.selection.OneItemDetailLookup
import com.idanatz.oneadapter.internal.selection.OneItemKeyProvider
import com.idanatz.oneadapter.internal.selection.SelectionTrackerObserver
import com.idanatz.oneadapter.internal.selection.SelectionObserver
import com.idanatz.oneadapter.internal.swiping.OneItemTouchHelper
import com.idanatz.oneadapter.internal.utils.*
import com.idanatz.oneadapter.internal.validator.MissingConfigArgumentException
import com.idanatz.oneadapter.internal.utils.extensions.isClassExists
import com.idanatz.oneadapter.internal.utils.extensions.let2
import com.idanatz.oneadapter.internal.utils.extensions.removeAllItems
import com.idanatz.oneadapter.internal.utils.extensions.removeClassIfExist
import com.idanatz.oneadapter.internal.validator.Validator
import java.util.*

@Suppress("UNCHECKED_CAST", "NAME_SHADOWING")
internal class InternalAdapter(val recyclerView: RecyclerView) : RecyclerView.Adapter<OneViewHolder<Diffable>>(), LoadMoreObserver, SelectionObserver, ItemSelectionActionsProvider {

    companion object {
        const val UPDATE_DATA_DELAY_MILLIS = 100L
    }

    private val context
        get() = recyclerView.context

    internal val modules = Modules()
    internal var data: MutableList<Diffable> = mutableListOf()

    private val viewHolderCreatorsStore = ViewHolderCreatorsStore()

    // Paging
    private var endlessScrollListener: EndlessScrollListener? = null

    // Item Selection
    private var selectionTracker: SelectionTracker<Long>? = null
    private var itemKeyProvider: OneItemKeyProvider? = null

    // Handlers
    private val backgroundHandler = Handler(HandlerThread("OneAdapterBackgroundHandler").apply { start() }.looper)
    private val uiHandler
        get() = recyclerView.handler

    private val diffCallback = object : DiffUtilCallback {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.getUniqueIdentifier() == newItem.getUniqueIdentifier()
        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.areContentTheSame(newItem)
    }

    init {
        setHasStableIds(true)

        recyclerView.apply {
            adapter = this@InternalAdapter
            OneItemTouchHelper().attachToRecyclerView(this)
        }
    }

    //region Traditional Adapter Overrides
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneViewHolder<Diffable> {
        val oneViewHolder = viewHolderCreatorsStore.getCreator(viewType)?.create(parent)
        return oneViewHolder ?: throw RuntimeException("OneViewHolder creation failed")
    }

    override fun onBindViewHolder(holder: OneViewHolder<Diffable>, position: Int) {
        val model = data[position]
        Logger.logd {"onBindViewHolder -> position: $position, model: $model" }

        when (model) {
            is EmptyIndicator, is LoadingIndicator -> holder.onBindViewHolder(null)
            else -> holder.onBindViewHolder(model)
        }
    }

    private fun onBindSelection(holder: OneViewHolder<Diffable>, position: Int, selected: Boolean) {
        val model =
            when (val model = data[position]) {
                is EmptyIndicator, is LoadingIndicator -> null
                else -> model
            }
        Logger.logd {"onBindSelection -> position: $position, model: $model, selected: $selected" }
        holder.onBindSelection(model, selected)
    }

    override fun getItemCount() = data.size

    override fun getItemId(position: Int): Long  {
        val item = data[position]
        // javaClass is used for lettings different Diffable models share the same unique identifier
        return item.javaClass.simpleName.hashCode() + item.getUniqueIdentifier()
    }

    override fun getItemViewType(position: Int) = viewHolderCreatorsStore.getCreatorUniqueIndex(data[position].javaClass)

    override fun onViewRecycled(holder: OneViewHolder<Diffable>) {
        super.onViewRecycled(holder)
        holder.onUnbind(holder.model)
    }
    //endregion

    fun updateData(data: MutableList<Diffable>) {
        backgroundHandler.post {
            Validator.validateItemsAgainstRegisteredModules(viewHolderCreatorsStore, data)

            // modify the incoming data if needed
            if (data.isEmpty()) {
                if (modules.emptinessModule != null) {
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
    fun <M : Diffable> register(itemModule: ItemModule<M>) {
        val holderModuleConfig = itemModule.provideModuleConfig()
        val dataClass = extractGenericClass(itemModule.javaClass) ?: throw MissingConfigArgumentException("ItemModuleConfig missing model class")

        Validator.validateItemModuleAgainstRegisteredModules(viewHolderCreatorsStore, dataClass)
        Validator.validateLayoutExists(context, holderModuleConfig.withLayoutResource())

        viewHolderCreatorsStore.addCreator(dataClass, object : ViewHolderCreator<M> {
            override fun create(parent: ViewGroup): OneViewHolder<M> {
                return object : OneViewHolder<M>(parent, holderModuleConfig, itemModule.statesMap, itemModule.eventHooksMap) {
                    override fun onBind(model: M?) { model?.let { itemModule.onBind(it, viewBinder) } }
                    override fun onUnbind(model: M?) { model?.let { itemModule.onUnbind(it, viewBinder) } }
                }
            }
        } as ViewHolderCreator<Diffable>)
    }
    //endregion

    //region Emptiness Module
    fun enableEmptiness(emptinessModule: EmptinessModule) {
        modules.emptinessModule = emptinessModule
        viewHolderCreatorsStore.addCreator(EmptyIndicator.javaClass, object : ViewHolderCreator<Diffable> {
            override fun create(parent: ViewGroup): OneViewHolder<Diffable> {
                return object : OneViewHolder<Diffable>(parent, emptinessModule.provideModuleConfig()) {
                    override fun onBind(model: Diffable?) = emptinessModule.onBind(viewBinder)
                    override fun onUnbind(model: Diffable?) = emptinessModule.onUnbind(viewBinder)
                }
            }
        })
        configureEmptinessModule()
    }

    private fun configureEmptinessModule() {
        // in case emptiness module is configured, add empty indicator item
        if (data.isEmpty() && modules.emptinessModule != null) {
            data.add(0, EmptyIndicator)
        }
    }
    //endregion

    //region Paging Module
    fun enablePaging(pagingModule: PagingModule) {
        // save the module and the config for later use, e.g invoking onLoadMore callback
        pagingModule.pagingModuleConfig = pagingModule.provideModuleConfig()
        modules.pagingModule = pagingModule

        viewHolderCreatorsStore.addCreator(LoadingIndicator.javaClass, object : ViewHolderCreator<Diffable> {
            override fun create(parent: ViewGroup): OneViewHolder<Diffable> {
                return object : OneViewHolder<Diffable>(parent, pagingModule.pagingModuleConfig) {
                    override fun onBind(model: Diffable?) = pagingModule.onBind(viewBinder)
                    override fun onUnbind(model: Diffable?) = pagingModule.onUnbind(viewBinder)
                }
            }
        })
        configurePagingModule()
    }

    private fun configurePagingModule() {
        let2(recyclerView.layoutManager, modules.pagingModule) { layoutManager, loadMoreModule ->
            endlessScrollListener = EndlessScrollListener(
                    layoutManager = layoutManager,
                    visibleThreshold = loadMoreModule.pagingModuleConfig.withVisibleThreshold(),
                    includeEmptyState = modules.emptinessModule != null,
                    loadMoreObserver = this@InternalAdapter
            ).also { recyclerView.addOnScrollListener(it) }
        }
    }

    override fun onLoadingStateChanged(loading: Boolean) {
        if (loading) {
            data.isClassExists(LoadingIndicator.javaClass).takeIf { it == false }?.let {
                data.add(data.size, LoadingIndicator)

                // post it to the UI handler because the recycler crashes when calling notify from an onScroll callback
                uiHandler?.post { notifyItemInserted(data.size) }
            }
        }
    }

    override fun onLoadMore(currentPage: Int) {
        modules.pagingModule?.onLoadMore(currentPage)
    }
    //endregion

    //region Selection Module
    fun enableSelection(itemSelectionModule: ItemSelectionModule) {
        modules.itemSelectionModule = itemSelectionModule
        modules.actions.itemSelectionActions = ItemSelectionActions(this)
        configureItemSelectionModule()
    }

    private fun configureItemSelectionModule() {
        modules.itemSelectionModule?.let { selectionModule ->
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

    override fun onItemStateChanged(key: Long, selected: Boolean) {
        let2(recyclerView, itemKeyProvider) { recyclerView, itemKeyProvider ->
            recyclerView.findViewHolderForItemId(key)?.let { holder ->
                onBindSelection(holder as OneViewHolder<Diffable>, itemKeyProvider.getPosition(key), selected)
            }
        }
    }

    override fun onSelectionStateChanged() {
        selectionTracker?.let {
            modules.itemSelectionModule?.onSelectionUpdated(it.selection.size())
        }
    }

    override fun getSelectedItems(): List<Diffable> {
        return let2(selectionTracker, itemKeyProvider) { selectionTracker, itemKeyProvider ->
            selectionTracker.selection?.map { key -> itemKeyProvider.getPosition(key) }?.filter { it >= 0 }?.map { position -> data[position] } ?: emptyList()
        } ?: emptyList()
    }

    override fun removeSelectedItems() {
        val dataCopy = LinkedList(data).apply { removeAllItems(getSelectedItems()) }
        updateData(dataCopy)

        uiHandler?.postDelayed({
            clearSelection()
        }, UPDATE_DATA_DELAY_MILLIS)
    }

    override fun clearSelection() = selectionTracker?.clearSelection()
    //endregion

    //region RecyclerView
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        endlessScrollListener?.let { recyclerView.removeOnScrollListener(it) }
    }
    //endregion
}