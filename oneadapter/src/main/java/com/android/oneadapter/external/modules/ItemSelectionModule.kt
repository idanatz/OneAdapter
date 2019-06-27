package com.android.oneadapter.external.modules

import com.android.oneadapter.internal.selection.ItemSelectionActionsProvider

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