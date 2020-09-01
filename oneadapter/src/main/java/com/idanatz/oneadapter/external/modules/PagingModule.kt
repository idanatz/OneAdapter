package com.idanatz.oneadapter.external.modules

import android.animation.Animator
import com.idanatz.oneadapter.external.*
import com.idanatz.oneadapter.external.interfaces.*

open class PagingModule {

	internal var config: PagingModuleConfig by SingleAssignmentDelegate(DefaultPagingModuleConfig())
	internal var onCreate: OnCreated? = null
	internal var onBind: OnBinded? = null
	internal var onUnbind: OnUnbinded? = null
	internal var onLoadMore: OnMoreLoaded? = null

	fun config(block: PagingModuleConfigDsl.() -> Unit) {
		PagingModuleConfigDsl(config).apply(block).build().also { config = it }
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

	fun onLoadMore(block: OnMoreLoaded) {
		onLoadMore = block
	}
}

interface PagingModuleConfig : LayoutModuleConfig {
	var visibleThreshold: Int
}

class PagingModuleConfigDsl internal constructor(defaultConfig: PagingModuleConfig) : PagingModuleConfig by defaultConfig {

	fun build(): PagingModuleConfig = object : PagingModuleConfig {
		override var layoutResource: Int? = this@PagingModuleConfigDsl.layoutResource
		override var firstBindAnimation: Animator? = this@PagingModuleConfigDsl.firstBindAnimation
		override var visibleThreshold: Int = this@PagingModuleConfigDsl.visibleThreshold
	}
}

private class DefaultPagingModuleConfig : PagingModuleConfig {
	override var layoutResource: Int? = null
	override var firstBindAnimation: Animator? = null
	override var visibleThreshold: Int = 1
}