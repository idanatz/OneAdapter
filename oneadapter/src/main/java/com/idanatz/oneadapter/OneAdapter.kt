package com.idanatz.oneadapter

import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.interfaces.*
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.oneadapter.internal.InternalAdapter
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

    fun <M : Any> attachItemModule(itemModule: ItemModule<M>): com.idanatz.oneadapter.OneAdapter {
        internalAdapter.register(itemModule)
        return this
    }

    fun attachPagingModule(pagingModule: PagingModule): com.idanatz.oneadapter.OneAdapter {
        internalAdapter.enablePaging(pagingModule)
        return this
    }

    fun attachEmptinessModule(emptinessModule: EmptinessModule): com.idanatz.oneadapter.OneAdapter {
        internalAdapter.enableEmptiness(emptinessModule)
        return this
    }

    fun attachItemSelectionModule(itemSelectionModule: ItemSelectionModule): com.idanatz.oneadapter.OneAdapter {
        internalAdapter.enableSelection(itemSelectionModule)
        return this
    }

    fun attachTo(recyclerView: RecyclerView): com.idanatz.oneadapter.OneAdapter {
        internalAdapter.attachTo(recyclerView)
        return this
    }

    private fun getIndexOfItem(itemToFind: Any): Int {
        return internalItems.indexOfFirst { item ->
            when {
                item is Diffable && itemToFind is Diffable -> item.getUniqueIdentifier() == itemToFind.getUniqueIdentifier()
                else -> item == itemToFind
            }
        }
    }
}