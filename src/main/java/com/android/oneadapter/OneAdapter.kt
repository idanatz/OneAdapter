package com.android.oneadapter

import android.support.v7.widget.RecyclerView
import com.android.oneadapter.internal.InternalAdapter
import com.android.oneadapter.interfaces.LoadMoreInjector
import com.android.oneadapter.interfaces.Diffable
import com.android.oneadapter.interfaces.EmptyInjector
import com.android.oneadapter.interfaces.HolderInjector
import java.util.*

/**
 * Created by Idan Atsmon on 19/11/2018.
 */

class OneAdapter {

    private val internalAdapter: InternalAdapter = InternalAdapter.create()

    private val internalItems: List<Any>
        get() = internalAdapter.data

    fun setItems(items: List<Any>) {
        internalAdapter.updateData(LinkedList(items))
    }

    fun clear() {
        internalAdapter.updateData(listOf())
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

    fun <T : Any> injectHolder(holderInjector: HolderInjector<T>): OneAdapter {
        internalAdapter.register(holderInjector)
        return this
    }

    fun injectLoadMoreHolder(loadMoreInjector: LoadMoreInjector): OneAdapter {
        internalAdapter.enableLoadMore(loadMoreInjector)
        return this
    }

    fun injectEmptyHolder(emptyInjector: EmptyInjector): OneAdapter {
        internalAdapter.enableEmptyState(emptyInjector)
        return this
    }

    fun attachTo(recyclerView: RecyclerView): OneAdapter {
        internalAdapter.attachTo(recyclerView)
        return this
    }

    private fun getIndexOfItem(itemToFind: Any): Int {
        return internalItems.indexOfFirst { item ->
            when (item) {
                is Diffable -> item.areItemsTheSame(itemToFind)
                else -> item == itemToFind
            }
        }
    }
}