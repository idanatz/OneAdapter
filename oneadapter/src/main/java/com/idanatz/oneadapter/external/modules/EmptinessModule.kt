package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.internal.interfaces.InternalModuleConfig
import org.jetbrains.annotations.NotNull

abstract class EmptinessModule {

    abstract fun provideModuleConfig(): EmptinessModuleConfig
    open fun onBind(@NotNull viewBinder: ViewBinder) {}
    open fun onUnbind(@NotNull viewBinder: ViewBinder) {}
}

abstract class EmptinessModuleConfig : InternalModuleConfig()