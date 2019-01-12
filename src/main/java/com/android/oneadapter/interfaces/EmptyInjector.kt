package com.android.oneadapter.interfaces

import com.android.oneadapter.internal.ViewInteractor

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class EmptyInjector : HolderInjector<Any>() {

    // there is no data for empty injector, so use empty implementation
    override fun onInject(data: Any, viewInteractor: ViewInteractor) {}
}