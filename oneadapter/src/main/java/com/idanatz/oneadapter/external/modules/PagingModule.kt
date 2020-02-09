package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.external.holders.LoadingIndicator
import com.idanatz.oneadapter.external.interfaces.*
import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.internal.holders.ViewBinder

abstract class PagingModule :
        LayoutModuleConfigurable<PagingModuleConfig>,
        Creatable, Bindable<LoadingIndicator>, Unbindable<LoadingIndicator>
{

    // lifecycle
    override fun onCreated(@NotNull viewBinder: ViewBinder) {}
    override fun onBind(item: Item<LoadingIndicator>, viewBinder: ViewBinder) {}
    override fun onUnbind(item: Item<LoadingIndicator>, viewBinder: ViewBinder) {}

    // functionality
    abstract fun onLoadMore(currentPage: Int)
}

abstract class PagingModuleConfig : LayoutModuleConfig {
    abstract fun withVisibleThreshold() : Int
}