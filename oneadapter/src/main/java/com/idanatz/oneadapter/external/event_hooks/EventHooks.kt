package com.idanatz.oneadapter.external.event_hooks

import com.idanatz.oneadapter.external.OnClicked
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.external.OnSwiped
import com.idanatz.oneadapter.external.OnSwipedCompleted
import com.idanatz.oneadapter.external.SingleAssignmentDelegate
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHook.*
import com.idanatz.oneadapter.external.interfaces.Config

sealed class EventHook<M : Diffable>

open class ClickEventHook<M : Diffable> : EventHook<M>() {

	internal var onClick: OnClicked<M>? = null

	fun onClick(block: OnClicked<M>) {
		onClick = block
	}

	companion object {
		val TAG: String = ClickEventHook::class.java.simpleName
	}
}

open class SwipeEventHook<M : Diffable> : EventHook<M>() {

	internal var config: SwipeEventHookConfig by SingleAssignmentDelegate(DefaultSwipeEventHookConfig())
	internal var onSwipe: OnSwiped? = null
	internal var onSwipeComplete: OnSwipedCompleted<M>? = null

	fun config(block: SwipeEventHookConfigDsl.() -> Unit) {
		SwipeEventHookConfigDsl(config).apply(block).build().also { config = it }
	}

	fun onSwipe(block: OnSwiped) {
		onSwipe = block
	}

	fun onSwipeComplete(block: OnSwipedCompleted<M>) {
		onSwipeComplete = block
	}

	companion object {
		val TAG: String = SwipeEventHook::class.java.simpleName
	}

	enum class SwipeDirection {
		Start, End, Up, Down, None
	}
}

interface SwipeEventHookConfig : Config {
	val swipeDirection: List<SwipeDirection>
}

class SwipeEventHookConfigDsl internal constructor(defaultConfig: SwipeEventHookConfig) : SwipeEventHookConfig {

	override var swipeDirection: List<SwipeDirection> = defaultConfig.swipeDirection

	fun build(): SwipeEventHookConfig = object : SwipeEventHookConfig {
		override val swipeDirection: List<SwipeDirection> = this@SwipeEventHookConfigDsl.swipeDirection
	}
}

private class DefaultSwipeEventHookConfig : SwipeEventHookConfig {
	override val swipeDirection: List<SwipeDirection> = listOf(SwipeDirection.Start, SwipeDirection.End, SwipeDirection.Up, SwipeDirection.Down)
}