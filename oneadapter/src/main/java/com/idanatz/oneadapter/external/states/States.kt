package com.idanatz.oneadapter.external.states

import org.jetbrains.annotations.NotNull

sealed class State<M>

abstract class SelectionState<M> : State<M>() {
    companion object {
        val TAG: String = SelectionState::class.java.simpleName
    }
    abstract fun isSelectionEnabled(@NotNull model: M): Boolean
    open fun onSelected(@NotNull model: M, selected: Boolean) {}
}