package com.idanatz.oneadapter.external.event_hooks

import android.graphics.Canvas
import com.idanatz.oneadapter.external.interfaces.BehaviorHookConfig
import com.idanatz.oneadapter.external.interfaces.BehaviourHookConfigurable
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.internal.holders.ViewBinder
import org.jetbrains.annotations.NotNull

sealed class EventHook<M : Diffable>

abstract class ClickEventHook<M : Diffable> : EventHook<M>() {
	companion object {
		val TAG: String = ClickEventHook::class.java.simpleName
	}

	abstract fun onClick(@NotNull item: Item<M>, @NotNull viewBinder: ViewBinder)
}

abstract class SwipeEventHook<M : Diffable>
	: EventHook<M>(), BehaviourHookConfigurable<SwipeEventHookConfig> {

	companion object {
		val TAG: String = SwipeEventHook::class.java.simpleName
	}

	abstract fun onSwipe(canvas: Canvas, xAxisOffset: Float, @NotNull viewBinder: ViewBinder)
	abstract fun onSwipeComplete(@NotNull item: Item<M>, @NotNull direction: SwipeDirection, @NotNull viewBinder: ViewBinder)

	enum class SwipeDirection {
		Right, Left
	}
}

abstract class SwipeEventHookConfig : BehaviorHookConfig {

	abstract fun withSwipeDirection(): List<SwipeEventHook.SwipeDirection>
}