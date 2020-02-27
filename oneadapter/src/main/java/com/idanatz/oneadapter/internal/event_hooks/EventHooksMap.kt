package com.idanatz.oneadapter.internal.event_hooks

import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.event_hooks.EventHook
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHook
import com.idanatz.oneadapter.external.interfaces.Diffable
import java.util.*

internal class EventHooksMap<M : Diffable> : HashMap<String, EventHook<M>>() {

    // Click
    fun getClickEventHook() = get(ClickEventHook.TAG) as ClickEventHook<M>?
    fun putClickEventHook(state: ClickEventHook<M>) = put(ClickEventHook.TAG, state)
    fun isClickEventHookConfigured() = getClickEventHook() != null

    // Swipe
    fun getSwipeEventHook() = get(SwipeEventHook.TAG) as SwipeEventHook<M>?
    fun putSwipeEventHook(state: SwipeEventHook<M>) = put(SwipeEventHook.TAG, state)
    fun isSwipeEventHookConfigured() = getSwipeEventHook() != null
}