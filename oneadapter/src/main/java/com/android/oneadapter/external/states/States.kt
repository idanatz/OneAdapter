package com.android.oneadapter.external.states

import androidx.annotation.NonNull

sealed class State<M>

abstract class SelectionState<M> : State<M>() {
    companion object {
        val TAG: String = SelectionState::class.java.simpleName
    }
    abstract fun selectionEnabled(@NonNull model: M): Boolean
    abstract fun onSelected(@NonNull model: M, selected: Boolean)
}