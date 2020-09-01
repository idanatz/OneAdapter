package com.idanatz.oneadapter.external.modules

import android.animation.Animator
import com.idanatz.oneadapter.external.SingleAssignmentDelegate
import com.idanatz.oneadapter.external.OnModelBinded
import com.idanatz.oneadapter.external.OnCreated
import com.idanatz.oneadapter.external.OnModelUnbinded
import com.idanatz.oneadapter.external.interfaces.*
import com.idanatz.oneadapter.external.event_hooks.EventHooksMap
import com.idanatz.oneadapter.external.states.StatesMap

abstract class ItemModule<M : Diffable> {

    internal var config: ItemModuleConfig by SingleAssignmentDelegate(DefaultItemModuleConfig())
    internal var onCreate: OnCreated? = null
    internal var onBind: OnModelBinded<M>? = null
    internal var onUnbind: OnModelUnbinded<M>? = null

    val states = StatesMap<M>()
    val eventHooks = EventHooksMap<M>()

    fun config(block: ItemModuleConfigDsl.() -> Unit) {
        ItemModuleConfigDsl(config).apply(block).build().also { config = it }
    }

    fun onCreate(block: OnCreated) {
        onCreate = block
    }

    fun onBind(block: OnModelBinded<M>) {
        onBind = block
    }

    fun onUnbind(block: OnModelUnbinded<M>) {
        onUnbind = block
    }
}

interface ItemModuleConfig : LayoutModuleConfig

class ItemModuleConfigDsl internal constructor(defaultConfig: ItemModuleConfig) : ItemModuleConfig by defaultConfig {

    fun build(): ItemModuleConfig = object : ItemModuleConfig {
        override var layoutResource: Int? = this@ItemModuleConfigDsl.layoutResource
        override var firstBindAnimation: Animator? = this@ItemModuleConfigDsl.firstBindAnimation
    }
}

private class DefaultItemModuleConfig : ItemModuleConfig {
    override var layoutResource: Int? = null
    override var firstBindAnimation: Animator? = null
}