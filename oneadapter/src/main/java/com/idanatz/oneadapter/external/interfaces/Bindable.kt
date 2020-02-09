package com.idanatz.oneadapter.external.interfaces

import com.idanatz.oneadapter.internal.holders.ViewBinder
import org.jetbrains.annotations.NotNull

interface Bindable<M : Diffable> {
    fun onBind(@NotNull item: Item<M>, @NotNull viewBinder: ViewBinder)
}