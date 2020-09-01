package com.idanatz.oneadapter.external.states

class StatesMap<M> : HashMap<String, State<M>>() {

    operator fun plusAssign(eventHook: State<M>) {
        when (eventHook) {
            is SelectionState -> put(SelectionState.TAG, eventHook)
        }
    }

    fun getSelectionState() = get(SelectionState.TAG) as SelectionState<M>?
    fun isSelectionStateConfigured() = getSelectionState() != null
}