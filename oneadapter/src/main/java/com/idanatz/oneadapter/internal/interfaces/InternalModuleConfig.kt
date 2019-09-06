package com.idanatz.oneadapter.internal.interfaces

import android.animation.Animator
import androidx.annotation.LayoutRes

abstract class InternalModuleConfig {
    @LayoutRes abstract fun withLayoutResource(): Int
    open fun withFirstBindAnimation(): Animator? = null
}