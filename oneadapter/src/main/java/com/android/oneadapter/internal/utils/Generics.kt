package com.android.oneadapter.internal.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal fun extractGenericClass(classWithGenericType: Class<*>) = getClass(getClassName(getGenericType(classWithGenericType)))
internal fun getGenericType(claszz: Class<*>) = (claszz.genericSuperclass as ParameterizedType).actualTypeArguments[0]
internal fun getClass(className: String) = Class.forName(className)

internal fun getClassName(type: Type?): String {
    val className = type.toString()
    if (className.startsWith("class ")) {
        return className.substring("class ".length)
    }
    return className
}