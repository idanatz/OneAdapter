package com.android.oneadapter.interfaces

import android.support.annotation.LayoutRes
import com.android.oneadapter.internal.ViewInteractor
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Idan Atsmon on 19/11/2018.
 */

abstract class HolderInjector<in M> {

    @LayoutRes abstract fun layoutResource(): Int
    abstract fun onInject(data: M, viewInteractor: ViewInteractor)

    fun extractActualTypeArguments(): Type? {
        val interfaces = this.javaClass.genericInterfaces
        for (type in interfaces) {
            if (type is ParameterizedType) {
                if (type.rawType == HolderInjector::class.java) {
                    val actualType = type.actualTypeArguments[0]
                    return actualType as? Class<*> ?: throw IllegalArgumentException("Could not get generic type argument of HolderInjector")
                }
            }
        }
        return null
    }
}