package com.android.oneadapter.interfaces

import com.android.oneadapter.internal.ViewFinder
import com.android.oneadapter.internal.holder_config.HolderConfig

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class HolderInjector<M> {

    abstract fun provideHolderConfig(): HolderConfig<M>
    abstract fun onInject(model: M, viewFinder: ViewFinder)
}