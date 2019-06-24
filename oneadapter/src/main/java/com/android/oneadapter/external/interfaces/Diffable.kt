package com.android.oneadapter.external.interfaces

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
interface Diffable {
    fun getUniqueIdentifier(): Long
    fun areContentTheSame(other: Any): Boolean
}