package com.idanatz.oneadapter.internal.diffing

import androidx.recyclerview.widget.DiffUtil
import com.idanatz.oneadapter.internal.interfaces.DiffUtilCallback

internal class OneDiffUtil constructor(
        private val oldData: List<Any>?,
        private val newData: List<Any>?,
        private val diffCallback: DiffUtilCallback
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldData?.size ?: 0

    override fun getNewListSize() = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (oldData?.get(oldItemPosition) != null && newData?.get(newItemPosition) != null)
            diffCallback.areItemsTheSame(oldData[oldItemPosition], newData[newItemPosition])
        else
            false

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        if (oldData?.get(oldItemPosition) != null && newData?.get(newItemPosition) != null)
            diffCallback.areContentsTheSame(oldData[oldItemPosition], newData[newItemPosition])
        else
            false
}