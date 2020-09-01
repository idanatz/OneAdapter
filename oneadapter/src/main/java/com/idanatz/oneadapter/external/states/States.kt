package com.idanatz.oneadapter.external.states

import com.idanatz.oneadapter.external.OnSelected
import com.idanatz.oneadapter.external.SingleAssignmentDelegate
import com.idanatz.oneadapter.external.interfaces.Config

sealed class State<M>

open class SelectionState<M> : State<M>() {

    internal var config: SelectionStateConfig by SingleAssignmentDelegate(DefaultSelectionStateConfig())
    internal var onSelected: OnSelected<M>? = null

    fun config(block: SelectionStateConfigDsl.() -> Unit) {
        SelectionStateConfigDsl(config).apply(block).build().also { config = it }
    }

    fun onSelected(block: OnSelected<M>) {
        onSelected = block
    }

    companion object {
        val TAG: String = SelectionState::class.java.simpleName
    }
}

interface SelectionStateConfig : Config {
    val enabled: Boolean
}

class SelectionStateConfigDsl internal constructor(defaultConfig: SelectionStateConfig) : SelectionStateConfig {

    override var enabled: Boolean = defaultConfig.enabled

    fun build(): SelectionStateConfig = object : SelectionStateConfig {
        override val enabled: Boolean = this@SelectionStateConfigDsl.enabled
    }
}

private class DefaultSelectionStateConfig : SelectionStateConfig {
    override val enabled: Boolean = true
}