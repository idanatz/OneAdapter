package com.android.oneadapter.modules.load_more

import androidx.annotation.NonNull
import com.android.oneadapter.internal.holders.ViewFinder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class LoadMoreModule {

    internal lateinit var loadMoreModuleConfig: LoadMoreModuleConfig

    abstract fun provideModuleConfig(builder: LoadMoreModuleConfig.Builder): LoadMoreModuleConfig
    abstract fun onLoadMore(currentPage: Int)
    open fun onUnbind(@NonNull viewFinder: ViewFinder) {}
}