package com.idanatz.oneadapter.internal.selection

import com.idanatz.oneadapter.external.interfaces.Diffable

interface ItemSelectionActions {

    fun getSelectedItems(): List<Diffable>
    fun isItemSelected(position: Int): Boolean
    fun removeSelectedItems()
    fun clearSelection(): Boolean?
}