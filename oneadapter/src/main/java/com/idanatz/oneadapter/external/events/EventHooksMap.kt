package com.idanatz.oneadapter.external.events

internal class EventHooksMap<M> : HashMap<String, EventHook<M>>() {

    fun getClickEventHook() = get(ClickEventHook.TAG) as ClickEventHook<M>?
    fun putClickEventHook(state: ClickEventHook<M>) = put(ClickEventHook.TAG, state)
}