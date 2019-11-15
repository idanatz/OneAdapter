package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.external.interfaces.*
import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.internal.holders.ViewBinder

abstract class PagingModule :
        LayoutConfigurable<PagingModuleConfig>, BehaviourConfigurable<PagingModuleConfig>,
        Creatable, Bindable, Unbindable
{

    // lifecycle
    override fun onCreated(@NotNull viewBinder: ViewBinder) {}
    override fun onBind(@NotNull viewBinder: ViewBinder) {}
    override fun onUnbind(@NotNull viewBinder: ViewBinder) {}

    // functionality
    abstract fun onLoadMore(currentPage: Int)
}

abstract class PagingModuleConfig : LayoutModuleConfig, BehaviorModuleConfig {
    abstract fun withVisibleThreshold() : Int
}