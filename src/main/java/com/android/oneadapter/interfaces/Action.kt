package com.android.oneadapter.interfaces

import android.view.View

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
interface Action<V : View> {
    fun run(view: V)
}