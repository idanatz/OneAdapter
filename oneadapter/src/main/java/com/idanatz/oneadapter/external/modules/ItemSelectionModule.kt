package com.idanatz.oneadapter.external.modules

import com.idanatz.oneadapter.internal.selection.ItemSelectionActionsProvider

abstract class ItemSelectionModule {

    abstract fun provideModuleConfig(): ItemSelectionModuleConfig
    open fun onSelectionUpdated(selectedCount: Int) {}
}

abstract class ItemSelectionModuleConfig {

    abstract fun withSelectionType(): SelectionType

    enum class SelectionType {
        Single, Multiple
    }
}

class ItemSelectionActions internal constructor(provider: ItemSelectionActionsProvider)
    : ItemSelectionActionsProvider by provider