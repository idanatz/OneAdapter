package com.idanatz.oneadapter.internal.selection

import com.idanatz.oneadapter.external.interfaces.Diffable

internal interface ItemSelectionActionsProvider {

    fun getSelectedItems(): List<Diffable>
    fun removeSelectedItems()
    fun clearSelection(): Boolean?
}