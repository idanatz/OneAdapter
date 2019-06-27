package com.android.oneadapter.external.modules

import androidx.annotation.NonNull
import com.android.oneadapter.internal.holders.ViewBinder
import com.android.oneadapter.internal.interfaces.InternalModuleConfig

abstract class PagingModule {

    internal lateinit var pagingModuleConfig: PagingModuleConfig

    abstract fun provideModuleConfig(): PagingModuleConfig
    abstract fun onLoadMore(currentPage: Int)
    open fun onBind(@NonNull viewBinder: ViewBinder) {}
    open fun onUnbind(@NonNull viewBinder: ViewBinder) {}
}

abstract class PagingModuleConfig : InternalModuleConfig() {
    abstract fun withVisibleThreshold() : Int
}