package com.android.oneadapter.internal.selection

import androidx.annotation.Nullable
import androidx.recyclerview.selection.ItemDetailsLookup

internal class OneItemDetail(private val adapterPosition: Int, private val selectionKey: Long) : ItemDetailsLookup.ItemDetails<Long>() {

    override fun getPosition(): Int = adapterPosition
    @Nullable override fun getSelectionKey(): Long = selectionKey
}