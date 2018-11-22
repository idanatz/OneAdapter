package com.android.oneadapter.utils

import com.android.oneadapter.interfaces.HolderInjector
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Idan Atsmon on 21/11/2018.
 */

internal class TypeExtractor {

    companion object {

        fun <T> getViewInjectorActualTypeArguments(holderInjector: HolderInjector<T>): Type? {
            val interfaces = holderInjector.javaClass.genericInterfaces
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

        fun isSameType(type: Type, targetType: Type): Boolean {
            if (type is Class<*> && targetType is Class<*>) {
                if (type.isAssignableFrom(targetType)) {
                    return true
                }
            } else if (type is ParameterizedType && targetType is ParameterizedType) {
                if (isSameType(type.rawType, targetType.rawType)) {
                    val types = type.actualTypeArguments
                    val targetTypes = targetType.actualTypeArguments
                    if (types.size != targetTypes.size) {
                        return false
                    }
                    val len = types.size
                    for (i in 0 until len) {
                        if (!isSameType(types[i], targetTypes[i])) {
                            return false
                        }
                    }
                    return true
                }
            }
            return false
        }
    }
}