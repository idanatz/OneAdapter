package com.idanatz.oneadapter.internal.selection

import android.view.MotionEvent
import androidx.annotation.Nullable
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.internal.holders.OneViewHolder
import org.jetbrains.annotations.NotNull

internal class OneItemDetailLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

    @Nullable
    override fun getItemDetails(@NotNull e: MotionEvent): ItemDetails<Long>? {
        return recyclerView.findChildViewUnder(e.x, e.y)?.let {
            val viewHolder = recyclerView.getChildViewHolder(it) as OneViewHolder<*>
            return viewHolder.createItemLookupInformation()
        }
    }
}