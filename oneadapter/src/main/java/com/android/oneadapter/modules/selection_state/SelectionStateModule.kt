package com.android.oneadapter.modules.selection_state

abstract class SelectionStateModule {

    abstract fun provideModuleConfig(): SelectionModuleConfig
    open fun onSelectionModeStarted() {}
    open fun onSelectionModeEnded() {}
}