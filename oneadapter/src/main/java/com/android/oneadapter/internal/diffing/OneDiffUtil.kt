package com.android.oneadapter.internal.diffing

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.android.oneadapter.interfaces.DiffUtilCallback

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
internal class OneDiffUtil internal constructor(
        private val oldData: List<Any>?,
        private val newData: List<Any>?,
        private val diffCallback: DiffUtilCallback
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldData?.size ?: 0

    override fun getNewListSize() = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (oldData?.get(oldItemPosition) != null && newData?.get(newItemPosition) != null) {
            diffCallback.areItemsTheSame(oldData[oldItemPosition], newData[newItemPosition])
        } else {
            Log.d("OneAdapter", "areItemsTheSame -> false, old: $oldItemPosition, new: $newItemPosition")
            false
        }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (oldData?.get(oldItemPosition) != null && newData?.get(newItemPosition) != null) {
            diffCallback.areContentsTheSame(oldData[oldItemPosition], newData[newItemPosition])
        } else {
            Log.d("OneAdapter", "areContentsTheSame -> false, old: $oldItemPosition, new: $newItemPosition")
            false
        }
}