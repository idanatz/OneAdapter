package com.android.oneadapter.internal.selection

internal interface SelectionObserver {
    fun onItemStateChanged(key: Long, selected: Boolean)
    fun onSelectionStateChanged()
}