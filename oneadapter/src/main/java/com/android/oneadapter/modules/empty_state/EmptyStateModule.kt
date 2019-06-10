package com.android.oneadapter.modules.empty_state

import androidx.annotation.NonNull
import com.android.oneadapter.internal.holders.ViewBinder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class EmptyStateModule {

    abstract fun provideModuleConfig(builder: EmptyStateModuleConfig.Builder): EmptyStateModuleConfig
    open fun onBind(@NonNull viewBinder: ViewBinder) {}
    open fun onUnbind(@NonNull viewBinder: ViewBinder) {}
}