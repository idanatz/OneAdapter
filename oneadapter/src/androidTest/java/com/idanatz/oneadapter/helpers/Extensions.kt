package com.idanatz.oneadapter.helpers

import android.view.View

fun View.getViewLocationOnScreen(): Pair<Float, Float> {
	val location = IntArray(2)
	getLocationOnScreen(location)
	return Pair(location[0].toFloat(), location[1].toFloat())
}