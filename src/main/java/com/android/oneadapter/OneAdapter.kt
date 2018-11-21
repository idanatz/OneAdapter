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
        internalAdapter.updateData(items)
    }

    fun clear() {
        internalAdapter.updateData(listOf())
    }

    fun add(item: Any) {
        add(internalItems.lastIndex, item)
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
        val indexToRemove = getElementIndex(item)
        if (indexToRemove != -1) {
            remove(indexToRemove)
        }
    }

    fun update(item: Any) {
        val indexToSet = getElementIndex(item)
        if (indexToSet != -1) {
            update(indexToSet, item)
        }
    }

    fun update(index: Int, item: Any) {
        val modifiedList = LinkedList(internalItems).apply { set(index, item) }
        internalAdapter.updateData(modifiedList)
    }

    fun <T> injectHolder(holderInjector: HolderInjector<T>): OneAdapter {
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

    fun disableDiffFunction(): OneAdapter {
        internalAdapter.disableDiff()
        return this
    }

    private fun getElementIndex(itemToFind: Any): Int {
        internalItems.forEachIndexed { index, item: Any ->
            when (item) {
                is Diffable -> if (item.areItemsTheSame(itemToFind)) return index
                else -> if (item == itemToFind) return index
            }
        }
        return -1
    }
}