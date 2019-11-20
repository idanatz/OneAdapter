package com.idanatz.oneadapter.external.interfaces

interface LayoutModuleConfigurable<C : LayoutModuleConfig>{
    fun provideModuleConfig(): C
}

interface BehaviourModuleConfigurable<C : BehaviorModuleConfig> {
    fun provideModuleConfig(): C
}

interface BehaviourHookConfigurable<C : BehaviorHookConfig> {
    fun provideHookConfig(): C
}