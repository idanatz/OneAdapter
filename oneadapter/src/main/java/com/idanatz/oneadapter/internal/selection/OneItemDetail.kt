package com.idanatz.oneadapter.internal.selection

import android.view.MotionEvent
import androidx.annotation.Nullable
import androidx.recyclerview.selection.ItemDetailsLookup
import com.idanatz.oneadapter.external.states.SelectionStateConfig

internal class OneItemDetail(
        private val adapterPosition: Int,
        private val selectionKey: Long,
        private val selectionTrigger: SelectionStateConfig.SelectionTrigger
) : ItemDetailsLookup.ItemDetails<Long>() {

    override fun getPosition(): Int = adapterPosition
    @Nullable override fun getSelectionKey(): Long = selectionKey

    // or: should any click trigger selection?
    override fun inSelectionHotspot(e: MotionEvent): Boolean = when (selectionTrigger) {
        SelectionStateConfig.SelectionTrigger.Click -> true
        SelectionStateConfig.SelectionTrigger.LongClick, SelectionStateConfig.SelectionTrigger.Manual -> false
    }
}