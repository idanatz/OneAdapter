package com.android.oneadapter.external.modules

import androidx.annotation.NonNull
import com.android.oneadapter.external.events.ClickEventHook
import com.android.oneadapter.external.events.EventHook
import com.android.oneadapter.external.events.EventHooksMap
import com.android.oneadapter.internal.holders.ViewBinder
import com.android.oneadapter.internal.interfaces.InternalModuleConfig
import com.android.oneadapter.external.states.SelectionState
import com.android.oneadapter.external.states.State
import com.android.oneadapter.external.states.StatesMap

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class ItemModule<M> {

    internal val statesMap = StatesMap<M>()
    internal val eventHooksMap = EventHooksMap<M>()

    abstract fun provideModuleConfig(): ItemModuleConfig<M>
    abstract fun onBind(@NonNull model: M, @NonNull viewBinder: ViewBinder)

    open fun onUnbind(@NonNull viewBinder: ViewBinder) {}

    fun addState(state: State<M>): ItemModule<M> {
        when (state) {
            is SelectionState -> statesMap.putSelectionState(state)
        }
        return this
    }

    fun addEventHook(eventHook: EventHook<M>): ItemModule<M> {
        when (eventHook) {
            is ClickEventHook -> eventHooksMap.putClickEventHook(eventHook)
        }
        return this
    }
}

abstract class ItemModuleConfig<M> : InternalModuleConfig<M>()