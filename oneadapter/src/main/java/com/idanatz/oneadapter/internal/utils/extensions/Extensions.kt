package com.idanatz.oneadapter.internal.utils.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

internal inline fun <A : Any?, B : Any?, R> let2(v1: A?, v2: B?, callback: (A, B) -> R): R? {
    return v1?.let { v1Verified ->
        v2?.let { v2Verified ->
            callback(v1Verified, v2Verified)
        }
    }
}

/**
 * Extension method to inflate layoutId from a given context
 */
internal fun Context.inflateLayout(@LayoutRes layoutId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false): View
        = LayoutInflater.from(this).inflate(layoutId, parent, attachToRoot)