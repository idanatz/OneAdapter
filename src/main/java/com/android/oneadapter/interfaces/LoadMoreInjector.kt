package com.android.oneadapter.interfaces

import com.android.oneadapter.internal.ViewFinder
import com.android.oneadapter.internal.holder_config.LoadMoreHolderConfig

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class LoadMoreInjector {

    abstract fun provideHolderConfig(): LoadMoreHolderConfig
    abstract fun onLoadMore(currentPage: Int)
    fun onUnbind(viewFinder: ViewFinder) {}
}