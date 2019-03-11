package com.android.oneadapter.interfaces

import android.support.annotation.LayoutRes
import android.view.View
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Idan Atsmon on 19/11/2018.
 */

abstract class HolderInjector<M> {

    @LayoutRes abstract fun layoutResource(): Int
    abstract fun onInject(data: M, inflatedView: View)

    fun extractActualTypeArguments(): Type? {
        val genericType = this.javaClass.genericSuperclass
        if (genericType is ParameterizedType && genericType.rawType == HolderInjector::class.java) {
            val actualType = genericType.actualTypeArguments[0]
            return actualType as? Class<*> ?: throw IllegalArgumentException("Could not get generic type argument of HolderInjector")
        }
        return null
    }
}