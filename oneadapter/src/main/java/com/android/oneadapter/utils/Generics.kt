package com.android.oneadapter.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal fun extractGenericClass(clazz: Class<*>): Class<*>? {
    return getClass(getClassName(getGenericType(clazz)))
}

internal fun getGenericType(claszz: Class<*>): Type? {
    return (claszz.genericSuperclass as ParameterizedType).actualTypeArguments[0]
}

internal fun getClass(className: String): Class<*>? {
    return Class.forName(className)
}

internal fun getClassName(type: Type?): String {
    val className = type.toString()
    if (className.startsWith("class ")) {
        return className.substring("class ".length)
    }
    return className
}