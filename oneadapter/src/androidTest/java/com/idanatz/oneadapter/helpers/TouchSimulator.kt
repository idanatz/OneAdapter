package com.idanatz.oneadapter.helpers

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import androidx.test.core.view.MotionEventBuilder

class TouchSimulator(private val handler: Handler) {

	fun simulateTap(onView: View, x: Float, y: Float) {
		val downEvent = MotionEventBuilder.newBuilder().setAction(MotionEvent.ACTION_DOWN).setPointer(x, y).build()
		onView.dispatchTouchEvent(downEvent)
		val upEvent = MotionEventBuilder.newBuilder().setAction(MotionEvent.ACTION_UP).setPointer(x, y).build()
		onView.dispatchTouchEvent(upEvent)
	}

	fun simulateLongTap(onView: View, x: Float, y: Float) {
		val downEvent = MotionEventBuilder.newBuilder().setAction(MotionEvent.ACTION_DOWN).setPointer(x, y).build()
		onView.dispatchTouchEvent(downEvent)

		handler.postDelayed ({
			val upEvent = MotionEventBuilder.newBuilder().setAction(MotionEvent.ACTION_UP).setPointer(x, y).build()
			onView.dispatchTouchEvent(upEvent)
		}, 50)
	}
}