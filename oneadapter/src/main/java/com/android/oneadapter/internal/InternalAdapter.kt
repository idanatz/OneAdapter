package com.android.oneadapter.internal

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.android.oneadapter.interfaces.*
import com.android.oneadapter.internal.diffing.OneDiffUtil
import com.android.oneadapter.internal.holders.EmptyIndicator
import com.android.oneadapter.internal.holders.LoadingIndicator
import com.android.oneadapter.internal.holders.OneViewHolder
import com.android.oneadapter.internal.load_more.EndlessScrollListener
import com.android.oneadapter.internal.load_more.OneEndlessScrollListener
import com.android.oneadapter.modules.selection_state.SelectionType
import com.android.oneadapter.internal.selection.OneItemDetailLookup
import com.android.oneadapter.internal.selection.OneItemKeyProvider
import com.android.oneadapter.internal.selection.OneSelectionObserver
import com.android.oneadapter.internal.selection.SelectionObserver
import com.android.oneadapter.modules.empty_state.EmptyStateModule
import com.android.oneadapter.modules.empty_state.EmptyStateModuleConfig
import com.android.oneadapter.modules.holder.HolderModule
import com.android.oneadapter.modules.holder.HolderModuleConfig
import com.android.oneadapter.modules.load_more.LoadMoreModule
import com.android.oneadapter.modules.load_more.LoadMoreModuleConfig
import com.android.oneadapter.modules.selection_state.SelectionModuleConfig
import com.android.oneadapter.modules.selection_state.SelectionStateModule
import com.android.oneadapter.utils.MissingBuilderArgumentException
import com.android.oneadapter.utils.MultipleHolderConflictException
import com.android.oneadapter.utils.let2
import com.android.oneadapter.utils.removeClassIfExist
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Idan Atsmon on 20/11/2018.
 */
@Suppress("UNCHECKED_CAST")
internal class InternalAdapter : RecyclerView.Adapter<OneViewHolder<Any>>(),
        EndlessScrollListener, SelectionObserver {

    private var recyclerView: RecyclerView? = null
    var data: MutableList<Any> = mutableListOf()
        private set

    private val dataTypes = ArrayList<Class<*>>() // maps T.class to unique indexes for adapter's getItemViewType
    private val holderCreators = HashMap<Class<*>, ViewHolderCreator<Any>>() // maps T.class -> ViewHolderCreator<T>

    // load more state
    private var loadMoreCreator: ViewHolderCreator<Any>? = null
    private var loadMoreModule: LoadMoreModule? = null
    private var oneEndlessScrollListener: OneEndlessScrollListener? = null

    // empty state
    private var emptyStateCreator: ViewHolderCreator<Any>? = null

    // selection state
    private var selectionStateModule: SelectionStateModule? = null
    private var selectionTracker: SelectionTracker<Long>? = null
    private var itemKeyProvider: OneItemKeyProvider? = null
    private var duringSelection = false

    // handlers
    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }
    private val backgroundHandler by lazy { Handler(HandlerThread("BackgroundHandler").apply { start() }.looper) }

    private val diffCallback =
            object : DiffUtilCallback {
                override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.getUniqueIdentifier() == newItem.getUniqueIdentifier()
                override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.areContentTheSame(newItem)
            }

    init {
        setHasStableIds(true)
    }

    //region Traditional Adapter Stuff
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneViewHolder<Any> {
        return when (viewType) {
            EmptyIndicator.getType() -> emptyStateCreator!!.create(parent)
            LoadingIndicator.getType() -> loadMoreCreator!!.create(parent)
            else -> {
                val dataType = dataTypes[viewType]
                val creator = holderCreators[dataType]

                if (creator == null) {
                    throw IllegalArgumentException("$dataType has no associated Module attached")
                } else {
                    return creator.create(parent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OneViewHolder<Any>, position: Int) {
        val model =
            when (val model = data[position]) {
                is EmptyIndicator, is LoadingIndicator -> null
                else -> model
            }

        Log.d("OneAdapter", "onBindViewHolder -> position: $position, model: $model")
        holder.onBindViewHolder(model)
    }

    private fun onBindSelection(holder: OneViewHolder<Any>, position: Int, selected: Boolean) {
        val model =
            when (val model = data[position]) {
                is EmptyIndicator, is LoadingIndicator -> null
                else -> model
            }
        Log.d("OneAdapter", "onBindSelection -> position: $position, model: $model, selected: $selected")
        holder.onBindSelection(model, selected)
    }

    override fun getItemCount(): Int = data.size

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
                if (emptyStateCreator != null) {
                    data.add(0, EmptyIndicator())
                }
                oneEndlessScrollListener?.resetState()
            } else {
                data.removeClassIfExist(EmptyIndicator::class.java)
                data.removeClassIfExist(LoadingIndicator::class.java)
            }

            // handle the diffing
            val diffResult = DiffUtil.calculateDiff(OneDiffUtil(this.data, data, diffCallback))
            this.data = data
            uiHandler.post { diffResult.dispatchUpdatesTo(this) }
        }
    }

    fun <M : Any> register(holderModule: HolderModule<M>) {
        val holderModuleConfig = holderModule.provideModuleConfig(HolderModuleConfig.Builder())
        val dataClass = holderModuleConfig.modelClass ?: throw MissingBuilderArgumentException("HolderModuleConfig missing model class")

        if (holderCreators.containsKey(dataClass)) {
            throw MultipleHolderConflictException("HolderModule with model class ${dataClass.simpleName} already attached")
        }

        holderCreators[dataClass] = object : ViewHolderCreator<M> {
            override fun create(parent: ViewGroup): OneViewHolder<M> {
                return object : OneViewHolder<M>(parent, holderModuleConfig) {
                    override fun onBind(model: M?) { model?.let { holderModule.onBind(it, viewFinder) } }
                    override fun onUnbind() = holderModule.onUnbind(viewFinder)
                    override fun onSelected(model: M?) { if (selectionStateModule != null) model?.let { holderModule.onSelected(it, true) } }
                    override fun onUnSelected(model: M?) { if (selectionStateModule != null) model?.let { holderModule.onSelected(it, false) } }
                }
            }
        } as ViewHolderCreator<Any>
    }

    //region Empty State Module
    fun enableEmptyState(emptyStateModule: EmptyStateModule) {
        emptyStateCreator = object : ViewHolderCreator<Any> {
            override fun create(parent: ViewGroup): OneViewHolder<Any> {
                return object : OneViewHolder<Any>(parent, emptyStateModule.provideModuleConfig(EmptyStateModuleConfig.Builder())) {
                    override fun onBind(model: Any?) = emptyStateModule.onBind(viewFinder)
                    override fun onUnbind() = emptyStateModule.onUnbind(viewFinder)
                    override fun onSelected(model: Any?) {}
                    override fun onUnSelected(model: Any?) {}
                }
            }
        }
    }
    //endregion

    //region Load More Module
    fun enableLoadMore(loadMoreModule: LoadMoreModule) {
        // save the module and the config for later use, e.g invoking onLoadMore callback
        loadMoreModule.loadMoreModuleConfig = loadMoreModule.provideModuleConfig(LoadMoreModuleConfig.Builder())
        this.loadMoreModule = loadMoreModule

        loadMoreCreator = object : ViewHolderCreator<Any> {
            override fun create(parent: ViewGroup): OneViewHolder<Any> {
                return object : OneViewHolder<Any>(parent, loadMoreModule.loadMoreModuleConfig) {
                    override fun onBind(model: Any?) {}
                    override fun onUnbind() = loadMoreModule.onUnbind(viewFinder)
                    override fun onSelected(model: Any?) {}
                    override fun onUnSelected(model: Any?) {}
                }
            }
        }
    }

    override fun onLoadingStateChanged(loading: Boolean) {
        if (loading) {
            data.indexOfFirst { it is LoadingIndicator }.let { index ->
                if (index == -1) {
                    data.add(data.size, LoadingIndicator())

                    // post it to the UI handler because the recycler crashes when calling notify from an onScroll callback
                    uiHandler.post { notifyItemInserted(data.size) }
                }
            }
        }
    }

    override fun notifyLoadMore(currentPage: Int) {
        loadMoreModule?.onLoadMore(currentPage)
    }
    //endregion

    //region Selection Module
    fun enableSelection(selectionStateModule: SelectionStateModule) {
        this.selectionStateModule = selectionStateModule
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
            if (it.selection.isEmpty) {
                duringSelection = false
                selectionStateModule?.onSelectionModeEnded()
            } else {
                if (duringSelection) return else duringSelection = true
                selectionStateModule?.onSelectionModeStarted()
            }
        }
    }

    fun getSelectedItems(): List<Any> {
        return let2(selectionTracker, itemKeyProvider) { selectionTracker, itemKeyProvider ->
            selectionTracker.selection?.map { key -> itemKeyProvider.getPosition(key) }?.filter { it >= 0 }?.map { position -> data[position] } ?: emptyList()
        } ?: emptyList()
    }

    fun clearSelection() = selectionTracker?.clearSelection()

    //endregion

    //region RecyclerView Stuff
    fun attachTo(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView.apply {
            adapter = this@InternalAdapter
            configureLoadMoreModule(this)
            configureSelectionModule(this)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        oneEndlessScrollListener?.let { recyclerView.removeOnScrollListener(it) }
    }
    //endregion

    private fun configureLoadMoreModule(recyclerView: RecyclerView) {
        let2(recyclerView.layoutManager, loadMoreModule) { layoutManager, loadMoreModule ->
            oneEndlessScrollListener = OneEndlessScrollListener(
                    layoutManager = layoutManager,
                    visibleThreshold = loadMoreModule.loadMoreModuleConfig.visibleThreshold,
                    includeEmptyState = emptyStateCreator != null,
                    endlessScrollListener = this@InternalAdapter
            ).also { recyclerView.addOnScrollListener(it) }
        }
    }

    private fun configureSelectionModule(recyclerView: RecyclerView) {
        selectionStateModule?.let { selectionModule ->
            selectionTracker = SelectionTracker.Builder<Long>(
                    recyclerView.id.toString(),
                    recyclerView,
                    OneItemKeyProvider(recyclerView).also { itemKeyProvider = it },
                    OneItemDetailLookup(recyclerView),
                    StorageStrategy.createLongStorage()
            )
            .withSelectionPredicate(when (selectionModule.provideModuleConfig(SelectionModuleConfig.Builder()).selectionType) {
                SelectionType.Single -> SelectionPredicates.createSelectSingleAnything()
                SelectionType.Multiple -> SelectionPredicates.createSelectAnything()
            })
            .build()
            .also { it.addObserver(OneSelectionObserver(this@InternalAdapter)) }
        }
    }
}