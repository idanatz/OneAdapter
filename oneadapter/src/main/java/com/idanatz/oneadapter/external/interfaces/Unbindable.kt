package com.idanatz.oneadapter.external.interfaces

import com.idanatz.oneadapter.internal.holders.ViewBinder
import org.jetbrains.annotations.NotNull

interface Unbindable<M : Diffable> {
    fun onUnbind(@NotNull item: Item<M>, @NotNull viewBinder: ViewBinder)
}