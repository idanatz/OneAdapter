package com.idanatz.oneadapter.internal.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.idanatz.oneadapter.internal.holders.OneViewHolder
import java.lang.IllegalStateException

internal inline fun <A : Any?, B : Any?, R> let2(v1: A?, v2: B?, callback: (A, B) -> R): R? {
    return v1?.let { v1Verified ->
        v2?.let { v2Verified ->
            callback(v1Verified, v2Verified)
        }
    }
}

internal fun <T, M> MutableList<T>.removeClassIfExist(classToRemove: Class<M>) {
    indexOfFirst { classToRemove.isInstance(it) }.takeIf { it != -1 }?.let { foundIndex -> removeAt(foundIndex) }
}

internal fun RecyclerView.LayoutManager.findLastVisibleItemPosition(): Int {
    return when (this) {
        is LinearLayoutManager -> findLastVisibleItemPosition()
        is GridLayoutManager -> findLastVisibleItemPosition()
        is StaggeredGridLayoutManager -> {
            val lastVisibleItemPositions = findLastVisibleItemPositions(null)

            // get maximum element within the list
            var maxSize = 0
            for (i in lastVisibleItemPositions.indices) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i]
                } else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i]
                }
            }
            return maxSize
        }
        else -> throw IllegalStateException("Recycler view layout manager is not supported")
    }
}

internal fun RecyclerView.ViewHolder.toOneViewHolder() = this as OneViewHolder<*>

/**
 * Extension method to inflate layoutId from a given context
 */
internal fun Context.inflateLayout(@LayoutRes layoutId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View
        = android.view.LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)