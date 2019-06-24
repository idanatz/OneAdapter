package com.android.oneadapter.internal.interfaces

/**
 * Created by Idan Atsmon on 20/11/2018.
 */
internal interface DiffUtilCallback {
    fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean
    fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean
}