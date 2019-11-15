package com.idanatz.oneadapter.external.interfaces

import com.idanatz.oneadapter.internal.holders.ViewBinder
import org.jetbrains.annotations.NotNull

interface ModelUnbindable<M> {
    fun onUnbind(@NotNull model: M, @NotNull viewBinder: ViewBinder)
}

interface Unbindable {
    fun onUnbind(@NotNull viewBinder: ViewBinder)
}