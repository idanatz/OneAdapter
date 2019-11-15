package com.idanatz.oneadapter.external.modules

import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.external.events.ClickEventHook
import com.idanatz.oneadapter.external.events.EventHook
import com.idanatz.oneadapter.external.events.EventHooksMap
import com.idanatz.oneadapter.external.events.SwipeEventHook
import com.idanatz.oneadapter.external.interfaces.*
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.external.states.State
import com.idanatz.oneadapter.external.states.StatesMap

abstract class ItemModule<M> :
        LayoutConfigurable<ItemModuleConfig>,
        Creatable, ModelBindable<M>, ModelUnbindable<M>
{

    internal val statesMap = StatesMap<M>()
    internal val eventHooksMap = EventHooksMap<M>()

    // lifecycle
    override fun onCreated(@NotNull viewBinder: ViewBinder) {}
    override fun onUnbind(@NotNull model: M, @NotNull viewBinder: ViewBinder) {}

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

abstract class ItemModuleConfig : LayoutModuleConfig