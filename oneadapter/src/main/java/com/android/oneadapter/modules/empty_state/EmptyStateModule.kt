package com.android.oneadapter.modules.empty_state

import androidx.annotation.NonNull
import com.android.oneadapter.internal.holders.ViewFinder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class EmptyStateModule {

    abstract fun provideModuleConfig(builder: EmptyStateModuleConfig.Builder): EmptyStateModuleConfig
    open fun onBind(@NonNull viewFinder: ViewFinder) {}
    open fun onUnbind(@NonNull viewFinder: ViewFinder) {}
}