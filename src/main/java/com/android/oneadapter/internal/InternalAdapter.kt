package com.android.oneadapter.internal

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.android.oneadapter.interfaces.*
import com.android.oneadapter.utils.let2
import com.android.oneadapter.utils.removeClassIfExist
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Idan Atsmon on 20/11/2018.
 */
internal class InternalAdapter :
        RecyclerView.Adapter<OneViewHolder<Any>>(),
        EndlessRecyclerViewScrollListener.InternalListener {

    var data: MutableList<Any> = mutableListOf()
        private set

    private val dataTypes = ArrayList<Class<*>>() // maps T.class to unique indexes for adapter's getItemViewType
    private val holderCreators = HashMap<Class<*>, ViewHolderCreator<Any>>() // maps T.class -> ViewHolderCreator<T>

    // endless scrolling
    private var loadMoreCreator: ViewHolderCreator<Any>? = null
    private var loadMoreInjector: LoadMoreInjector? = null
    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null

    // empty state
    private var emptyStateCreator: ViewHolderCreator<Any>? = null

    // handlers
    private val uiHandler by lazy { Handler(Looper.getMainLooper()) }
    private val backgroundHandler by lazy { Handler(HandlerThread("BackgroundHandler").apply { start() }.looper) }

    private val diffCallback: DiffUtilCallback by lazy {
        object : DiffUtilCallback {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.areItemsTheSame(newItem)
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.areContentTheSame(newItem)
            }
        }
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
                    throw IllegalArgumentException(String.format("Injector not found for model class %s...", dataType))
                } else {
                    return creator.create(parent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OneViewHolder<Any>, position: Int) {
        val item = data[position].run {
            when (this) {
                is EmptyIndicator, is LoadingIndicator -> null
                else -> this
            }
        }
        holder.onBindViewHolder(item)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = when {
        data[position] is EmptyIndicator -> EmptyIndicator.getType()
        data[position] is LoadingIndicator -> LoadingIndicator.getType()
        else -> {
            val item = data[position]
            if (dataTypes.indexOf(item.javaClass) == -1) {
                dataTypes.add(item.javaClass)
            }
            dataTypes.indexOf(item.javaClass)
        }
    }
    //endregion

    fun updateData(data: MutableList<Any>) {
        backgroundHandler.post {
            // modify the incoming data if needed
            if (data.isEmpty()) {
                if (emptyStateCreator != null) {
                    data.add(0, EmptyIndicator())
                }
                endlessScrollListener?.resetState()
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

    @Suppress("UNCHECKED_CAST")
    fun <M : Any> register(holderInjector: HolderInjector<M>) {
        val dataClass = holderInjector.provideHolderConfig().modelClass ?: throw IllegalArgumentException("must provide data class for holder injector")
        holderCreators[dataClass] = object : ViewHolderCreator<M> {
            override fun create(parent: ViewGroup): OneViewHolder<M> {
                return object : OneViewHolder<M>(parent, holderInjector.provideHolderConfig().layoutResource) {
                    override fun onBind(data: M, viewFinder: ViewFinder) {
                        holderInjector.onInject(data, viewFinder)
                    }
                }
            }
        } as ViewHolderCreator<Any>
    }

    fun enableLoadMore(loadMoreInjector: LoadMoreInjector) {
        // save the injector for later use, e.g invoking onLoadMore callback
        this.loadMoreInjector = loadMoreInjector

        loadMoreCreator = object : ViewHolderCreator<Any> {
            override fun create(parent: ViewGroup): OneViewHolder<Any> {
                return object : OneViewHolder<Any>(parent, loadMoreInjector.provideHolderConfig().layoutResource) {
                    override fun onBind(data: Any, viewFinder: ViewFinder) {}
                }
            }
        }
    }

    fun enableEmptyState(emptyInjector: EmptyInjector) {
        emptyStateCreator = object : ViewHolderCreator<Any> {
            override fun create(parent: ViewGroup): OneViewHolder<Any> {
                return object : OneViewHolder<Any>(parent, emptyInjector.provideHolderConfig().layoutResource) {
                    override fun onBind(data: Any, viewFinder: ViewFinder) {}
                }
            }
        }
    }

    //region RecyclerView Stuff
    fun attachTo(recyclerView: RecyclerView) {
        recyclerView.run {
            let2(layoutManager, loadMoreInjector) { layoutManager, loadMoreInjector ->
                endlessScrollListener = EndlessRecyclerViewScrollListener(
                        layoutManager = layoutManager,
                        visibleThreshold = loadMoreInjector.provideHolderConfig().visibleThreshold,
                        includeEmptyState = emptyStateCreator != null,
                        internalListener = this@InternalAdapter)
            }
            adapter = this@InternalAdapter
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        endlessScrollListener?.let { recyclerView.addOnScrollListener(it) }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        endlessScrollListener?.let { recyclerView.removeOnScrollListener(it) }
        super.onDetachedFromRecyclerView(recyclerView)
    }
    //endregion

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
        loadMoreInjector?.onLoadMore(currentPage)
    }
}