package com.android.oneadapter.modules.load_more

import com.android.oneadapter.internal.holders.ViewFinder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class LoadMoreModule {

    abstract fun provideModuleConfig(): LoadMoreModuleConfig
    abstract fun onLoadMore(currentPage: Int)
    open fun onUnbind(viewFinder: ViewFinder) {}
}