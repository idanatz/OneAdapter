package com.android.oneadapter.interfaces

/**
 * Created by Idan Atsmon on 20/12/2018.
 */
interface GenericRunnable<T> {
    fun run(view: T)
}