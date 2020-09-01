package com.idanatz.oneadapter.external

import kotlin.reflect.KProperty

class SingleAssignmentDelegate<T>(
		private var value: T,
		private var assigned: Boolean = false
) {

	operator fun getValue(thisRef: Any, property: KProperty<*>): T {
		return value
	}

	operator fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
		if (!assigned) {
			this.value = value
			assigned = true
		}
	}
}