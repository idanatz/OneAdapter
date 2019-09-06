package com.idanatz.oneadapter.external.modules

import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.internal.interfaces.InternalModuleConfig

abstract class PagingModule {

    abstract fun provideModuleConfig(): PagingModuleConfig
    abstract fun onLoadMore(currentPage: Int)
    open fun onBind(@NotNull viewBinder: ViewBinder) {}
    open fun onUnbind(@NotNull viewBinder: ViewBinder) {}
}

abstract class PagingModuleConfig : InternalModuleConfig() {
    abstract fun withVisibleThreshold() : Int
}