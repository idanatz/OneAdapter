package com.idanatz.oneadapter.external.states

internal class StatesMap<M> : HashMap<String, State<M>>() {

    fun getSelectionState() = get(SelectionState.TAG) as SelectionState<M>?
    fun putSelectionState(state: SelectionState<M>) = put(SelectionState.TAG, state)
}