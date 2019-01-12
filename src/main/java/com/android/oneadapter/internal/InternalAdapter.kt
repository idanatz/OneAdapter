package com.android.oneadapter.internal

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.android.oneadapter.interfaces.*
import com.android.oneadapter.utils.isSameType
import com.android.oneadapter.utils.let2
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Idan Atsmon on 20/11/2018.
 */
internal class InternalAdapter private constructor() : RecyclerView.Adapter<OneViewHolder<Any>>() {

    var data: List<Any> = listOf()
        private set

    private val dataTypes = ArrayList<Type>()
    private val holderCreators = HashMap<Type, ViewHolderCreator<Any>>()

    // endless scrolling
    private var loadMoreCreator: ViewHolderCreator<Any>? = null
    private var loadMoreInjector: LoadMoreInjector? = null
    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null

    // empty state
    private var emptyStateCreator: ViewHolderCreator<Any>? = null

    private val uiHandler = object : Handler(Looper.getMainLooper()) {
        override fun dispatchMessage(msg: Message) {
            when (msg.what) {
                EVENT_NOTIFY_DATA_SET_CHANGED -> notifyDataSetChanged()
            }
            super.dispatchMessage(msg)
        }
    }

    private var diffCallback: DiffUtilCallback? = object : DiffUtilCallback {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.areItemsTheSame(newItem)
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return oldItem is Diffable && newItem is Diffable && oldItem.javaClass == newItem.javaClass && oldItem.areContentTheSame(newItem)
        }
    }

    companion object {
        private const val EVENT_NOTIFY_DATA_SET_CHANGED = 999
        private const val TYPE_LOAD_MORE = -10
        private const val TYPE_EMPTY_STATE = -11

        fun create(): InternalAdapter {
            return InternalAdapter()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OneViewHolder<Any> {
        return when (viewType) {
            TYPE_EMPTY_STATE -> emptyStateCreator!!.create(parent)
            TYPE_LOAD_MORE -> loadMoreCreator!!.create(parent)
            else -> {
                val dataType = dataTypes[viewType]
                var creator = holderCreators[dataType]

                if (creator == null) {
                    for (type in holderCreators.keys) {
                        if (dataType.isSameType(type)) {
                            creator = holderCreators[type]
                            break
                        }
                    }
                }

                if (creator == null) {
                    throw IllegalArgumentException(String.format("TYPE: %s injector not found...", dataType))
                } else {
                    return creator.create(parent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: OneViewHolder<Any>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return if (data.isEmpty()) {
            if (emptyStateCreator != null) 1 else 0
        } else {
            data.size + if (loadMoreCreator != null) 1 else 0
        }
    }

    override fun getItemViewType(position: Int): Int = when {
        isInEmptyState() -> TYPE_EMPTY_STATE
        isInLoadMoreState(position) -> TYPE_LOAD_MORE
        else -> {
            val item = data[position]
            if (dataTypes.indexOf(item.javaClass) == -1) {
                dataTypes.add(item.javaClass)
            }
            dataTypes.indexOf(item.javaClass)
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

    fun updateData(data: List<Any>): InternalAdapter {
        if (data.isEmpty()) {
            endlessScrollListener?.resetState()
        }

        if (isInEmptyState()) { // special case, do not use diff utils here due to crash in recycler view
            this.data = data
            notifyItemRemoved(0) // remove the empty holder
            notifyItemRangeInserted(0, data.size) // insert new data holders
        } else {
            val diffResult = DiffUtil.calculateDiff(OneDiffUtil(this.data, data, diffCallback!!))
            this.data = data
            if (Looper.myLooper() == Looper.getMainLooper()) {
                diffResult.dispatchUpdatesTo(this)
            } else {
                uiHandler.removeMessages(EVENT_NOTIFY_DATA_SET_CHANGED)
                uiHandler.sendEmptyMessage(EVENT_NOTIFY_DATA_SET_CHANGED)
            }
        }
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <M : Any> register(holderInjector: HolderInjector<M>): InternalAdapter {
        val dataType = holderInjector.extractActualTypeArguments() ?: throw IllegalArgumentException()
        holderCreators[dataType] = object : ViewHolderCreator<M> {
            override fun create(parent: ViewGroup): OneViewHolder<M> {
                return object : OneViewHolder<M>(parent, holderInjector.layoutResource()) {
                    override fun onBind(data: M, viewInteractor: ViewInteractor) {
                        holderInjector.onInject(data, viewInteractor)
                    }
                }
            }
        } as ViewHolderCreator<Any>
        return this
    }

    fun enableLoadMore(loadMoreInjector: LoadMoreInjector): InternalAdapter {
        this.loadMoreInjector = loadMoreInjector

        loadMoreCreator = object : ViewHolderCreator<Any> {
            override fun create(parent: ViewGroup): OneViewHolder<Any> {
                return object : OneViewHolder<Any>(parent, loadMoreInjector.layoutResource()) {
                    override fun onBind(data: Any, viewInteractor: ViewInteractor) {}
                }
            }
        }
        return this
    }

    fun enableEmptyState(emptyInjector: EmptyInjector): InternalAdapter {
        emptyStateCreator = object : ViewHolderCreator<Any> {
            override fun create(parent: ViewGroup): OneViewHolder<Any> {
                return object : OneViewHolder<Any>(parent, emptyInjector.layoutResource()) {
                    override fun onBind(data: Any, viewInteractor: ViewInteractor) {}
                }
            }
        }
        return this
    }

    fun attachTo(recyclerView: RecyclerView): InternalAdapter {
        recyclerView.run {
            let2(layoutManager, loadMoreInjector) { layoutManager, loadMoreInjector ->
                endlessScrollListener = EndlessRecyclerViewScrollListener(layoutManager, loadMoreInjector, emptyStateCreator != null)
            }
            adapter = this@InternalAdapter
        }
        return this
    }

    private fun getItem(position: Int): Any? = when {
        isInLoadMoreState(position) -> null
        isInEmptyState() -> null
        else -> data[position]
    }

    private fun isInEmptyState() = data.isEmpty() && emptyStateCreator != null
    private fun isInLoadMoreState(position: Int) = loadMoreInjector != null && data.size <= position
}