package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.internal.interfaces.InternalModuleConfig
import org.jetbrains.annotations.NotNull

abstract class PagingModule {

    internal lateinit var pagingModuleConfig: PagingModuleConfig

    abstract fun provideModuleConfig(): PagingModuleConfig
    abstract fun onLoadMore(currentPage: Int)
    open fun onBind(@NotNull viewBinder: ViewBinder) {}
    open fun onUnbind(@NotNull viewBinder: ViewBinder) {}
}

abstract class PagingModuleConfig : InternalModuleConfig() {
    abstract fun withVisibleThreshold() : Int
}