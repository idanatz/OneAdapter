package com.idanatz.oneadapter

import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.oneadapter.internal.InternalAdapter
import com.idanatz.oneadapter.external.modules.Modules
import com.idanatz.oneadapter.internal.utils.extensions.getIndexOfItem
import com.idanatz.oneadapter.internal.utils.extensions.removeAllItems
import com.idanatz.oneadapter.internal.validator.MissingModuleDefinitionException
import com.idanatz.oneadapter.internal.validator.MultipleModuleConflictException
import java.util.*

class OneAdapter(recyclerView: RecyclerView) {

    internal val internalAdapter = InternalAdapter(recyclerView)

    private val internalItems: List<Diffable>
        get() = internalAdapter.data

    val modulesActions: Modules.Actions
        get() = internalAdapter.modules.actions

    val itemCount: Int
        get() = internalAdapter.itemCount

    /**
     * Sets the adapter's item list to the given list.
     * @throws MissingModuleDefinitionException if any of the given items are missing an ItemModule.
     */
    fun setItems(items: List<Diffable>) {
        internalAdapter.updateData(LinkedList(items))
    }

    /**
     * Sets the adapter's item list to an empty list.
     * Will trigger an EmptinessModule's onBind call if configured.
     * @see EmptinessModule
     */
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

    /**
     * Attach the given ItemModule to the adapter.
     * This will add the ability to process items of the ItemModule type.
     * @throws MultipleModuleConflictException if an ItemModule of the same type already exists.
     */
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

    fun getVisibleItemIndexes(requiredVisibilityPercentage: Float = 1f): List<Int> {
        return internalAdapter.holderVisibilityResolver.getIndexes(requiredVisibilityPercentage)
    }

    fun <M : Diffable> getVisibleItemIndexes(ofClass: Class<M>, requiredVisibilityPercentage: Float = 1f): List<Int> {
        return internalAdapter.holderVisibilityResolver.getIndexes(ofClass, requiredVisibilityPercentage)
    }

    fun <M : Diffable> getVisibleItems(ofClass: Class<M>, requiredVisibilityPercentage: Float = 1f): List<M> {
        return internalAdapter.holderVisibilityResolver.getItems(ofClass, requiredVisibilityPercentage)
    }

    fun getItemViewType(position: Int): Int {
        return internalAdapter.getItemViewType(position)
    }

    fun getItemViewTypeFromClass(clazz: Class<*>): Int? {
        return internalAdapter.getItemViewTypeFromClass(clazz)
    }
}