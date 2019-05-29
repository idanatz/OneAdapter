package com.android.oneadapter

import androidx.recyclerview.widget.RecyclerView
import com.android.oneadapter.interfaces.*
import com.android.oneadapter.internal.InternalAdapter
import com.android.oneadapter.modules.empty_state.EmptyStateModule
import com.android.oneadapter.modules.holder.HolderModule
import com.android.oneadapter.modules.load_more.LoadMoreModule
import com.android.oneadapter.modules.selection_state.SelectionStateModule
import java.util.*

/**
 * Created by Idan Atsmon on 19/11/2018.
 */

class OneAdapter {

    private val internalAdapter = InternalAdapter()

    private val internalItems: List<Any>
        get() = internalAdapter.data

    fun setItems(items: List<Any>) {
        internalAdapter.updateData(LinkedList(items))
    }

    fun clear() {
        internalAdapter.updateData(mutableListOf())
    }

    fun add(item: Any) {
        add(internalItems.size, item)
    }

    fun add(index: Int, item: Any) {
        val modifiedList = LinkedList(internalItems).apply { add(index, item) }
        internalAdapter.updateData(modifiedList)
    }

    fun add(items: List<Any>) {
        val modifiedList = LinkedList(internalItems).apply { addAll(items) }
        internalAdapter.updateData(modifiedList)
    }

    fun remove(index: Int) {
        val modifiedList = LinkedList(internalItems).apply { removeAt(index) }
        internalAdapter.updateData(modifiedList)
    }

    fun remove(item: Any) {
        val indexToRemove = getIndexOfItem(item)
        if (indexToRemove != -1) {
            remove(indexToRemove)
        }
    }

    fun update(item: Any) {
        val indexToSet = getIndexOfItem(item)
        if (indexToSet != -1) {
            val modifiedList = LinkedList(internalItems).apply { set(indexToSet, item) }
            internalAdapter.updateData(modifiedList)
        }
    }

    fun <M : Any> attachHolderModule(holderModule: HolderModule<M>): OneAdapter {
        internalAdapter.register(holderModule)
        return this
    }

    fun attachLoadMoreModule(loadMoreModule: LoadMoreModule): OneAdapter {
        internalAdapter.enableLoadMore(loadMoreModule)
        return this
    }

    fun attachEmptyStateModule(emptyStateModule: EmptyStateModule): OneAdapter {
        internalAdapter.enableEmptyState(emptyStateModule)
        return this
    }

    fun attachSelectionStateModule(selectionStateModule: SelectionStateModule): OneAdapter {
        internalAdapter.enableSelection(selectionStateModule)
        return this
    }

    fun attachTo(recyclerView: RecyclerView): OneAdapter {
        internalAdapter.attachTo(recyclerView)
        clear() // after attaching the recycler we init the adapter
        return this
    }

    fun getSelectedItems() = internalAdapter.getSelectedItems()
    fun clearSelection() = internalAdapter.clearSelection()

    private fun getIndexOfItem(itemToFind: Any): Int {
        return internalItems.indexOfFirst { item ->
            when {
                item is Diffable && itemToFind is Diffable -> item.getUniqueIdentifier() == itemToFind.getUniqueIdentifier()
                else -> item == itemToFind
            }
        }
    }
}