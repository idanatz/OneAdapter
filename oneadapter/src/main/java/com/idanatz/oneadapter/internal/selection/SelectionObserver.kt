package com.idanatz.oneadapter.internal.selection

internal interface SelectionObserver {
    fun onItemStateChanged(key: Long, selected: Boolean)
    fun onSelectionStateChanged()
}