package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.external.interfaces.*
import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.internal.holders.ViewBinder

abstract class EmptinessModule :
        LayoutConfigurable<EmptinessModuleConfig>,
        Creatable, Bindable, Unbindable
{

    // lifecycle
    override fun onCreated(@NotNull viewBinder: ViewBinder) {}
    override fun onBind(@NotNull viewBinder: ViewBinder) {}
    override fun onUnbind(@NotNull viewBinder: ViewBinder) {}
}

abstract class EmptinessModuleConfig : LayoutModuleConfig