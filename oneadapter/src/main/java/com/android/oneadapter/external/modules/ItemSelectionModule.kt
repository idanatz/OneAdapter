package com.android.oneadapter.external.modules

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