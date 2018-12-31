package com.android.oneadapter.interfaces

import android.support.annotation.LayoutRes
import com.android.oneadapter.internal.ViewInteractor

/**
 * Created by Idan Atsmon on 19/11/2018.
 */

interface HolderInjector<T> {
    @LayoutRes fun layoutResource(): Int
    fun onInject(data: T, viewInteractor: ViewInteractor)
}