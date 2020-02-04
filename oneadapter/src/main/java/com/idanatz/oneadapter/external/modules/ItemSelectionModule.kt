package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.external.interfaces.BehaviorModuleConfig
import com.idanatz.oneadapter.external.interfaces.BehaviourModuleConfigurable
import com.idanatz.oneadapter.internal.selection.ItemSelectionActions

abstract class ItemSelectionModule :
        BehaviourModuleConfigurable<ItemSelectionModuleConfig> {

    var actions: ItemSelectionActions? = null

    // functionality
    open fun onSelectionUpdated(selectedCount: Int) {}
}

abstract class ItemSelectionModuleConfig : BehaviorModuleConfig {

    abstract fun withSelectionType(): SelectionType

    enum class SelectionType {
        Single, Multiple
    }
}