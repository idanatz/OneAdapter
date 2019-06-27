package com.android.oneadapter.external.modules

import androidx.annotation.NonNull
import com.android.oneadapter.internal.holders.ViewBinder
import com.android.oneadapter.internal.interfaces.InternalModuleConfig

abstract class EmptinessModule {

    abstract fun provideModuleConfig(): EmptinessModuleConfig
    open fun onBind(@NonNull viewBinder: ViewBinder) {}
    open fun onUnbind(@NonNull viewBinder: ViewBinder) {}
}

abstract class EmptinessModuleConfig : InternalModuleConfig()