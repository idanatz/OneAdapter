package com.idanatz.oneadapter.external.interfaces

import com.idanatz.oneadapter.internal.holders.Metadata
import com.idanatz.oneadapter.internal.holders.ViewBinder
import org.jetbrains.annotations.NotNull

interface ModelUnbindable<M : Diffable> {
    fun onUnbind(@NotNull item: Item<M>, @NotNull viewBinder: ViewBinder)
}

interface Unbindable {
    fun onUnbind(@NotNull viewBinder: ViewBinder, @NotNull metadata: Metadata)
}