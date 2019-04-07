package com.android.oneadapter.interfaces

import com.android.oneadapter.internal.ViewFinder
import com.android.oneadapter.internal.holder_config.EmptyHolderConfig

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class EmptyInjector {

    abstract fun provideHolderConfig(): EmptyHolderConfig
    fun onUnbind(viewFinder: ViewFinder) {}
}