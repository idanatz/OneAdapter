package com.idanatz.oneadapter.external.interfaces

import com.idanatz.oneadapter.internal.holders.ViewBinder
import org.jetbrains.annotations.NotNull

interface ModelBindable<M> {
    fun onBind(@NotNull model: M, @NotNull viewBinder: ViewBinder)
}

interface Bindable {
    fun onBind(@NotNull viewBinder: ViewBinder)
}