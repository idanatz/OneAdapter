package com.android.oneadapter.modules.holder

import com.android.oneadapter.internal.holders.ViewFinder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class HolderModule<M> {

    abstract fun provideModuleConfig(): HolderModuleConfig<M>
    abstract fun onBind(model: M, viewFinder: ViewFinder)
    open fun onUnbind(viewFinder: ViewFinder) {}
    open fun onSelected(model: M, selected: Boolean) {}
}