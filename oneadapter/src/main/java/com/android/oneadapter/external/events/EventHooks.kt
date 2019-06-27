package com.android.oneadapter.external.events

import androidx.annotation.NonNull
import com.android.oneadapter.internal.holders.ViewBinder

sealed class EventHook<M>

abstract class ClickEventHook<M> : EventHook<M>() {
    companion object {
        val TAG: String = ClickEventHook::class.java.simpleName
    }

    abstract fun onClick(@NonNull model: M, @NonNull viewBinder: ViewBinder)
}