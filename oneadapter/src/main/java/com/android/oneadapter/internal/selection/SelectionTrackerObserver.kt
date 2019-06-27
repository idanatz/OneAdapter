package com.android.oneadapter.internal.selection

import androidx.recyclerview.selection.SelectionTracker

internal class SelectionTrackerObserver(private val selectionObserver: SelectionObserver) : SelectionTracker.SelectionObserver<Long>() {

    override fun onItemStateChanged(key: Long, selected: Boolean) = selectionObserver.onItemStateChanged(key, selected)
    override fun onSelectionChanged() = selectionObserver.onSelectionStateChanged()
}