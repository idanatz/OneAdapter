package com.idanatz.oneadapter.external.modules

import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.internal.interfaces.InternalModuleConfig

abstract class EmptinessModule {

    abstract fun provideModuleConfig(): EmptinessModuleConfig
    open fun onBind(@NotNull viewBinder: ViewBinder) {}
    open fun onUnbind(@NotNull viewBinder: ViewBinder) {}
}

abstract class EmptinessModuleConfig : InternalModuleConfig()