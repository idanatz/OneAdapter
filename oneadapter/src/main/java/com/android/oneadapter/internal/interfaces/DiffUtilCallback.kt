package com.android.oneadapter.internal.interfaces

internal interface DiffUtilCallback {
    fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean
    fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean
}