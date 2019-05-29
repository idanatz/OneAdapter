package com.android.oneadapter.internal.selection

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

internal class OneItemKeyProvider(private val recyclerView: RecyclerView) : ItemKeyProvider<Long>(SCOPE_MAPPED) {

    override fun getKey(position: Int): Long? = recyclerView.adapter?.getItemId(position)
    override fun getPosition(key: Long): Int = recyclerView.findViewHolderForItemId(key)?.layoutPosition ?: RecyclerView.NO_POSITION
}