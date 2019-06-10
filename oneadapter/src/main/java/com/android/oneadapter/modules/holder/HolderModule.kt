package com.android.oneadapter.modules.holder

import androidx.annotation.NonNull
import com.android.oneadapter.internal.holders.ViewBinder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
abstract class HolderModule<M> {

    abstract fun provideModuleConfig(builder: HolderModuleConfig.Builder<M>): HolderModuleConfig<M>
    abstract fun onBind(@NonNull model: M, @NonNull viewBinder: ViewBinder)
    open fun onUnbind(@NonNull viewBinder: ViewBinder) {}
    open fun onSelected(@NonNull model: M, selected: Boolean) {}
}