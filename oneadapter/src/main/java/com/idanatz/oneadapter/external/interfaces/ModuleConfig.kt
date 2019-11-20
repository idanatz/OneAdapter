package com.idanatz.oneadapter.external.interfaces

import android.animation.Animator
import androidx.annotation.LayoutRes

interface LayoutModuleConfig {
    @LayoutRes fun withLayoutResource(): Int
    fun withFirstBindAnimation(): Animator? = null
}

interface BehaviorModuleConfig
interface BehaviorHookConfig