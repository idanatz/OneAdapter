package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.external.OnSelectionEnded
import com.idanatz.oneadapter.external.OnSelectionStarted
import com.idanatz.oneadapter.external.OnSelectionUpdated
import com.idanatz.oneadapter.external.SingleAssignmentDelegate
import com.idanatz.oneadapter.external.interfaces.Config
import com.idanatz.oneadapter.external.modules.ItemSelectionModuleConfig.*
import com.idanatz.oneadapter.internal.selection.ItemSelectionActions

open class ItemSelectionModule {

    var actions: ItemSelectionActions? = null

    internal var config: ItemSelectionModuleConfig by SingleAssignmentDelegate(DefaultItemSelectionModuleConfig())
    internal var onStartSelection: OnSelectionStarted? = null
    internal var onUpdateSelection: OnSelectionUpdated? = null
    internal var onEndSelection: OnSelectionEnded? = null

    fun config(block: ItemSelectionModuleConfigDsl.() -> Unit) {
        ItemSelectionModuleConfigDsl(config).apply(block).build().also { config = it }
    }

    fun onStartSelection(block: OnSelectionStarted) {
        onStartSelection = block
    }

    fun onUpdateSelection(block: OnSelectionUpdated) {
        onUpdateSelection = block
    }

    fun onEndSelection(block: OnSelectionEnded) {
        onEndSelection = block
    }
}

interface ItemSelectionModuleConfig : Config {

    var selectionType: SelectionType

    enum class SelectionType {
        Single, Multiple
    }
}

class ItemSelectionModuleConfigDsl internal constructor(defaultConfig: ItemSelectionModuleConfig) : ItemSelectionModuleConfig by defaultConfig {

    fun build(): ItemSelectionModuleConfig = object : ItemSelectionModuleConfig {
        override var selectionType: SelectionType = this@ItemSelectionModuleConfigDsl.selectionType
    }
}

private class DefaultItemSelectionModuleConfig : ItemSelectionModuleConfig {
    override var selectionType: SelectionType = SelectionType.Single
}