package com.idanatz.oneadapter.external.modules

import android.animation.Animator
import com.idanatz.oneadapter.external.*
import com.idanatz.oneadapter.external.interfaces.*

open class EmptinessModule {

	internal var config: EmptinessModuleConfig by SingleAssignmentDelegate(DefaultEmptinessModuleConfig())
	internal var onCreate: OnCreated? = null
	internal var onBind: OnBinded? = null
	internal var onUnbind: OnUnbinded? = null

	fun config(block: EmptinessModuleConfigDsl.() -> Unit) {
		EmptinessModuleConfigDsl(config).apply(block).build().also { config = it }
	}

	fun onCreate(block: OnCreated) {
		onCreate = block
	}

	fun onBind(block: OnBinded) {
		onBind = block
	}

	fun onUnbind(block: OnUnbinded) {
		onUnbind = block
	}
}

interface EmptinessModuleConfig : LayoutModuleConfig

class EmptinessModuleConfigDsl internal constructor(defaultConfig: EmptinessModuleConfig) : EmptinessModuleConfig by defaultConfig {

	fun build(): EmptinessModuleConfig = object : EmptinessModuleConfig {
		override var layoutResource: Int? = this@EmptinessModuleConfigDsl.layoutResource
		override var firstBindAnimation: Animator? = this@EmptinessModuleConfigDsl.firstBindAnimation
	}
}

private class DefaultEmptinessModuleConfig : EmptinessModuleConfig {
	override var layoutResource: Int? = null
	override var firstBindAnimation: Animator? = null
}