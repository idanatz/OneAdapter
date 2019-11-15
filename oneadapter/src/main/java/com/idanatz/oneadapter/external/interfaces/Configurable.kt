package com.idanatz.oneadapter.external.interfaces

interface LayoutConfigurable<C : LayoutModuleConfig>{
    fun provideModuleConfig(): C
}

interface BehaviourConfigurable<C : BehaviorModuleConfig> {
    fun provideModuleConfig(): C
}