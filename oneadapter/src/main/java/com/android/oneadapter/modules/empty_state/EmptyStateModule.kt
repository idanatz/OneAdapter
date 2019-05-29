package com.android.oneadapter.modules.empty_state

import com.android.oneadapter.internal.holders.ViewFinder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class EmptyStateModule {

    abstract fun provideModuleConfig(): EmptyStateModuleConfig
    open fun onBind(viewFinder: ViewFinder) {}
    open fun onUnbind(viewFinder: ViewFinder) {}
}