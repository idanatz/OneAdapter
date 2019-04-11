package com.android.oneadapter.utils

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import java.lang.IllegalStateException

/**
 * Created by Idan Atsmon on 20/11/2018.
 */
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