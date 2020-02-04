package com.idanatz.oneadapter

import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.oneadapter.internal.InternalAdapter
import com.idanatz.oneadapter.external.modules.Modules
import com.idanatz.oneadapter.internal.utils.extensions.*
import com.idanatz.oneadapter.internal.utils.extensions.getIndexOfItem
import com.idanatz.oneadapter.external.MissingModuleDefinitionException
import com.idanatz.oneadapter.external.MultipleModuleConflictException
import com.idanatz.oneadapter.external.UnsupportedClassException

class OneAdapter(recyclerView: RecyclerView) {

    internal val internalAdapter = InternalAdapter(recyclerView)

    private val internalItems: List<Diffable>
        get() = internalAdapter.data

    val modules: Modules
        get() = internalAdapter.modules

    val itemCount: Int
        get() = internalAdapter.itemCount

    /**
     * Sets the adapter's item list to the given list.
     * @throws MissingModuleDefinitionException if any of the given items are missing an ItemModule.
     */
    fun setItems(items: List<Diffable>) {
        internalAdapter.updateData(items.createMutableCopy())
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
        val modifiedList = internalItems.createMutableCopyAndApply { add(index, item) }
        internalAdapter.updateData(modifiedList)
    }

    fun add(items: List<Diffable>) {
        val modifiedList = internalItems.createMutableCopyAndApply { addAll(items) }
        internalAdapter.updateData(modifiedList)
    }

    fun remove(index: Int) {
        val modifiedList = internalItems.createMutableCopyAndApply { removeAt(index) }
        internalAdapter.updateData(modifiedList)
    }

    fun remove(item: Diffable) {
        val indexToRemove = internalItems.getIndexOfItem(item)
        if (indexToRemove != -1) {
            remove(indexToRemove)
        }
    }

    fun remove(items: List<Diffable>) {
        val modifiedList = internalItems.createMutableCopyAndApply { removeAllItems(items) }
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

    @JvmOverloads
    fun getVisibleItemIndexes(requiredVisibilityPercentage: Float = 1f): List<Int> {
        return internalAdapter.holderVisibilityResolver.getIndexes(requiredVisibilityPercentage)
    }

    @JvmOverloads
    fun <M : Diffable> getVisibleItemIndexes(ofClass: Class<M>, requiredVisibilityPercentage: Float = 1f): List<Int> {
        return internalAdapter.holderVisibilityResolver.getIndexes(ofClass, requiredVisibilityPercentage)
    }

    @JvmOverloads
    fun <M : Diffable> getVisibleItems(ofClass: Class<M>, requiredVisibilityPercentage: Float = 1f): List<M> {
        return internalAdapter.holderVisibilityResolver.getItems(ofClass, requiredVisibilityPercentage)
    }

    fun getItemViewType(position: Int): Int {
        return internalAdapter.getItemViewType(position)
    }

    /**
     * Retrieves the view type of an item with a given class.
     * Note that this class must implement the Diffable interface and the adapter must contain items of that class.
     * @throws UnsupportedClassException if the class does not implement the Diffable interface
     * or is not registered as an Module data type.
     */
    fun getItemViewTypeFromClass(clazz: Class<*>): Int {
        return internalAdapter.getItemViewTypeFromClass(clazz)
    }
}