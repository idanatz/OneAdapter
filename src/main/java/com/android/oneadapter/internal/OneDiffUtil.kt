package com.android.oneadapter.internal

import android.support.v7.util.DiffUtil
import com.android.oneadapter.interfaces.DiffUtilCallback

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
internal class OneDiffUtil internal constructor(
        private val oldData: List<Any>?,
        private val newData: List<Any>?,
        private val diffCallback: DiffUtilCallback
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldData?.size ?: 0

    override fun getNewListSize(): Int = newData?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            if (oldData?.get(oldItemPosition) != null && newData?.get(newItemPosition) != null) {
                diffCallback.areItemsTheSame(oldData[oldItemPosition], newData[newItemPosition])
            } else {
                false
            }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            if (oldData?.get(oldItemPosition) != null && newData?.get(newItemPosition) != null) {
                diffCallback.areContentsTheSame(oldData[oldItemPosition], newData[newItemPosition])
            } else {
                false
            }
}