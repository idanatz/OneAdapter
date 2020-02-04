package com.idanatz.oneadapter.external.interfaces

import com.idanatz.oneadapter.internal.holders.Metadata
import com.idanatz.oneadapter.internal.holders.ViewBinder
import org.jetbrains.annotations.NotNull

interface ModelBindable<M : Diffable> {
    fun onBind(@NotNull item: Item<M>, @NotNull viewBinder: ViewBinder)
}

interface Bindable {
    fun onBind(@NotNull viewBinder: ViewBinder, @NotNull metadata: Metadata)
}