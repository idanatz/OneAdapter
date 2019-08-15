package com.idanatz.oneadapter.internal.selection

internal interface ItemSelectionActionsProvider {

    fun getSelectedItems(): List<Any>
    fun removeSelectedItems()
    fun clearSelection(): Boolean?
}