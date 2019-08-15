package com.idanatz.oneadapter.external.modules

import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.external.events.ClickEventHook
import com.idanatz.oneadapter.external.events.EventHook
import com.idanatz.oneadapter.external.events.EventHooksMap
import com.idanatz.oneadapter.external.events.SwipeEventHook
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.internal.interfaces.InternalModuleConfig
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.external.states.State
import com.idanatz.oneadapter.external.states.StatesMap

abstract class ItemModule<M> {

    internal val statesMap = StatesMap<M>()
    internal val eventHooksMap = EventHooksMap<M>()

    abstract fun provideModuleConfig(): ItemModuleConfig
    abstract fun onBind(@NotNull model: M, @NotNull viewBinder: ViewBinder)

    open fun onUnbind(@NotNull viewBinder: ViewBinder) {}

    fun addState(state: State<M>): ItemModule<M> {
        when (state) {
            is SelectionState -> statesMap.putSelectionState(state)
        }
        return this
    }

    fun addEventHook(eventHook: EventHook<M>): ItemModule<M> {
        when (eventHook) {
            is ClickEventHook -> eventHooksMap.putClickEventHook(eventHook)
            is SwipeEventHook -> eventHooksMap.putSwipeEventHook(eventHook)
        }
        return this
    }
}

abstract class ItemModuleConfig : InternalModuleConfig()