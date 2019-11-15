package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.external.interfaces.BehaviorModuleConfig
import com.idanatz.oneadapter.external.interfaces.BehaviourConfigurable
import com.idanatz.oneadapter.internal.selection.ItemSelectionActionsProvider

abstract class ItemSelectionModule :
        BehaviourConfigurable<ItemSelectionModuleConfig>
{

    // functionality
    open fun onSelectionUpdated(selectedCount: Int) {}
}

abstract class ItemSelectionModuleConfig : BehaviorModuleConfig{

    abstract fun withSelectionType(): SelectionType

    enum class SelectionType {
        Single, Multiple
    }
}

class ItemSelectionActions internal constructor(provider: ItemSelectionActionsProvider) : ItemSelectionActionsProvider by provider