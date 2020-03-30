package com.idanatz.oneadapter.internal.selection

import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.internal.holders.OneViewHolder

internal interface SelectionObserver {
    fun onItemStateChanged(holder: OneViewHolder<Diffable>, position: Int, selected: Boolean)
    fun onSelectionStarted()
    fun onSelectionEnded()
    fun onSelectionUpdated(selectedCount: Int)
}