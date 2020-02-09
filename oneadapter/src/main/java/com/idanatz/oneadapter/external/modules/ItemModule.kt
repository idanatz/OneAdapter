package com.idanatz.oneadapter.external.modules

import org.jetbrains.annotations.NotNull
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.event_hooks.EventHook
import com.idanatz.oneadapter.internal.event_hooks.EventHooksMap
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHook
import com.idanatz.oneadapter.external.interfaces.*
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.external.states.State
import com.idanatz.oneadapter.internal.holders.Metadata
import com.idanatz.oneadapter.internal.states.StatesMap

abstract class ItemModule<M : Diffable> :
        LayoutModuleConfigurable<ItemModuleConfig>,
        Creatable, Bindable<M>, Unbindable<M>
{
    internal val statesMap = StatesMap<M>()
    internal val eventHooksMap = EventHooksMap<M>()

    // lifecycle
    override fun onCreated(@NotNull viewBinder: ViewBinder) {}
    override fun onUnbind(item: Item<M>, viewBinder: ViewBinder) {}

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