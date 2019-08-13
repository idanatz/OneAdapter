package com.idanatz.oneadapter.external.events

internal class EventHooksMap<M> : HashMap<String, EventHook<M>>() {

    // Click
    fun getClickEventHook() = get(ClickEventHook.TAG) as ClickEventHook<M>?
    fun putClickEventHook(state: ClickEventHook<M>) = put(ClickEventHook.TAG, state)

    // Swipe
    fun getSwipeEventHook() = get(SwipeEventHook.TAG) as SwipeEventHook<M>?
    fun putSwipeEventHook(state: SwipeEventHook<M>) = put(SwipeEventHook.TAG, state)
}