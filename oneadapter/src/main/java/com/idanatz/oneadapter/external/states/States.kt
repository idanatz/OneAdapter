package com.idanatz.oneadapter.external.states

import com.idanatz.oneadapter.external.OnSelected
import com.idanatz.oneadapter.external.SingleAssignmentDelegate
import com.idanatz.oneadapter.external.interfaces.Config
import com.idanatz.oneadapter.external.states.SelectionStateConfig.*

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

    var enabled: Boolean
    var selectionTrigger: SelectionTrigger

    enum class SelectionTrigger {
        Click, // click on an Item will start a selection session
		LongClick, // long click on an Item will start a selection session
		Manual // selection session can be started only by calling the startSelection() function
    }
}

class SelectionStateConfigDsl internal constructor(defaultConfig: SelectionStateConfig) : SelectionStateConfig by defaultConfig {

    fun build(): SelectionStateConfig = object : SelectionStateConfig {
        override var enabled: Boolean = this@SelectionStateConfigDsl.enabled
        override var selectionTrigger: SelectionTrigger = this@SelectionStateConfigDsl.selectionTrigger
    }
}

private class DefaultSelectionStateConfig : SelectionStateConfig {
    override var enabled: Boolean = true
    override var selectionTrigger: SelectionTrigger = SelectionTrigger.LongClick
}