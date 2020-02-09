package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.external.holders.EmptyIndicator
import com.idanatz.oneadapter.external.interfaces.*
import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.internal.holders.ViewBinder

abstract class EmptinessModule :
        LayoutModuleConfigurable<EmptinessModuleConfig>,
        Creatable, Bindable<EmptyIndicator>, Unbindable<EmptyIndicator>
{

    // lifecycle
    override fun onCreated(@NotNull viewBinder: ViewBinder) {}
    override fun onBind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) {}
    override fun onUnbind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) {}
}

abstract class EmptinessModuleConfig : LayoutModuleConfig