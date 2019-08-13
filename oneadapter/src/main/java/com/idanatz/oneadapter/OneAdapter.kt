package com.idanatz.oneadapter

import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.oneadapter.internal.InternalAdapter
import com.idanatz.oneadapter.internal.utils.getIndexOfItem
import com.idanatz.oneadapter.internal.utils.removeAllItems
import java.util.*

class OneAdapter {

    private val internalAdapter = InternalAdapter()

    private val internalItems: List<Any>
        get() = internalAdapter.data

    val itemSelectionActions: ItemSelectionActions?
        get() = internalAdapter.modules.oneItemSelection?.actions

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
        val indexToRemove = getIndexOfItem(internalItems, item)
        if (indexToRemove != -1) {
            remove(indexToRemove)
        }
    }

    fun remove(items: List<Any>) {
        val modifiedList = LinkedList(internalItems).apply { removeAllItems(this, items) }
        internalAdapter.updateData(modifiedList)
    }

    fun update(item: Any) {
        val indexToSet = getIndexOfItem(internalItems, item)
        if (indexToSet != -1) {
            val modifiedList = LinkedList(internalItems).apply { set(indexToSet, item) }
            internalAdapter.updateData(modifiedList)
        }
    }

    fun <M : Any> attachItemModule(itemModule: ItemModule<M>): OneAdapter {
        internalAdapter.register(itemModule)
        return this
    }

    fun attachPagingModule(pagingModule: PagingModule): OneAdapter {
        internalAdapter.enablePaging(pagingModule)
        return this
    }

    fun attachEmptinessModule(emptinessModule: EmptinessModule): OneAdapter {
        internalAdapter.enableEmptiness(emptinessModule)
        return this
    }

    fun attachItemSelectionModule(itemSelectionModule: ItemSelectionModule): OneAdapter {
        internalAdapter.enableSelection(itemSelectionModule)
        return this
    }

    fun attachTo(recyclerView: RecyclerView): OneAdapter {
        internalAdapter.attachTo(recyclerView)
        return this
    }
}