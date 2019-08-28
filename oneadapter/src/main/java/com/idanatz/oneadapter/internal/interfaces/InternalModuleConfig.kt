package com.idanatz.oneadapter.internal.interfaces

import androidx.annotation.LayoutRes

abstract class InternalModuleConfig {
    @LayoutRes abstract fun withLayoutResource(): Int
}