package com.idanatz.oneadapter.external.interfaces

import com.idanatz.oneadapter.internal.holders.ViewBinder
import org.jetbrains.annotations.NotNull

interface Creatable {
    fun onCreated(@NotNull viewBinder: ViewBinder)
}