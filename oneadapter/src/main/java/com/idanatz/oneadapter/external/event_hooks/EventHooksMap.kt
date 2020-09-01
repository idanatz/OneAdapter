package com.idanatz.oneadapter.external.event_hooks

import com.idanatz.oneadapter.external.interfaces.Diffable
import java.util.*

class EventHooksMap<M : Diffable> : HashMap<String, EventHook<M>>() {

    operator fun plusAssign(eventHook: EventHook<M>) {
        when (eventHook) {
            is ClickEventHook -> put(ClickEventHook.TAG, eventHook)
            is SwipeEventHook -> put(SwipeEventHook.TAG, eventHook)
        }
    }

    // Click
    fun getClickEventHook() = get(ClickEventHook.TAG) as ClickEventHook<M>?
    fun isClickEventHookConfigured() = getClickEventHook() != null

    // Swipe
    fun getSwipeEventHook() = get(SwipeEventHook.TAG) as SwipeEventHook<M>?
    fun isSwipeEventHookConfigured() = getSwipeEventHook() != null
}