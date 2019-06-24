package com.android.oneadapter.external.modules

import com.android.oneadapter.internal.interfaces.InternalModuleConfig

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class PagingModule {

    internal lateinit var pagingModuleConfig: PagingModuleConfig

    abstract fun provideModuleConfig(): PagingModuleConfig
    abstract fun onLoadMore(currentPage: Int)
}

abstract class PagingModuleConfig : InternalModuleConfig<Any>() {
    abstract fun withVisibleThreshold() : Int
}