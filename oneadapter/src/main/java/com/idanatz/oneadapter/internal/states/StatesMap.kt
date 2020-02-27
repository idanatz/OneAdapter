package com.idanatz.oneadapter.internal.states

import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.external.states.State

internal class StatesMap<M> : HashMap<String, State<M>>() {

    fun getSelectionState() = get(SelectionState.TAG) as SelectionState<M>?
    fun putSelectionState(state: SelectionState<M>) = put(SelectionState.TAG, state)
    fun isSelectionStateConfigured() = getSelectionState() != null
}