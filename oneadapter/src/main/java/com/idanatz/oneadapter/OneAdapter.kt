package com.idanatz.oneadapter

import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.oneadapter.internal.InternalAdapter
import com.idanatz.oneadapter.internal.utils.extensions.getIndexOfItem
import com.idanatz.oneadapter.internal.utils.extensions.removeAllItems
import java.util.*

class OneAdapter {

    private val internalAdapter = InternalAdapter()

    private val internalItems: List<Diffable>
        get() = internalAdapter.data

    val itemSelectionActions: ItemSelectionActions?
        get() = internalAdapter.modules.oneItemSelection?.actions

    fun setItems(items: List<Diffable>) {
        internalAdapter.updateData(LinkedList(items))
    }

    fun clear() {
        internalAdapter.updateData(mutableListOf())
    }

    fun add(item: Diffable) {
        add(internalItems.size, item)
    }

    fun add(index: Int, item: Diffable) {
        val modifiedList = LinkedList(internalItems).apply { add(index, item) }
        internalAdapter.updateData(modifiedList)
    }

    fun add(items: List<Diffable>) {
        val modifiedList = LinkedList(internalItems).apply { addAll(items) }
        internalAdapter.updateData(modifiedList)
    }

    fun remove(index: Int) {
        val modifiedList = LinkedList(internalItems).apply { removeAt(index) }
        internalAdapter.updateData(modifiedList)
    }

    fun remove(item: Diffable) {
        val indexToRemove = internalItems.getIndexOfItem(item)
        if (indexToRemove != -1) {
            remove(indexToRemove)
        }
    }

    fun remove(items: List<Diffable>) {
        val modifiedList = LinkedList(internalItems).apply { removeAllItems(items) }
        internalAdapter.updateData(modifiedList)
    }

    fun update(item: Diffable) {
        val indexToSet = internalItems.getIndexOfItem(item)
        if (indexToSet != -1) {
            internalAdapter.notifyItemChanged(indexToSet)
        }
    }

    fun <M : Diffable> attachItemModule(itemModule: ItemModule<M>): OneAdapter {
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