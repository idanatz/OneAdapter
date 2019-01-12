package com.android.oneadapter.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by Idan Atsmon on 20/11/2018.
 */
internal inline fun <A : Any?, B : Any?, R> let2(v1: A?, v2: B?, callback: (A, B) -> R): R? {
    return v1?.let { v1Verified ->
        v2?.let { v2Verified ->
            callback(v1Verified, v2Verified)
        }
    }
}

internal fun Type.isSameType(targetType: Type): Boolean {
    if (this is Class<*> && targetType is Class<*>) {
        if (this.isAssignableFrom(targetType)) {
            return true
        }
    } else if (this is ParameterizedType && targetType is ParameterizedType) {
        if (this.rawType .isSameType(targetType.rawType)) {
            val types = this.actualTypeArguments
            val targetTypes = targetType.actualTypeArguments
            if (types.size != targetTypes.size) {
                return false
            }
            val len = types.size
            for (i in 0 until len) {
                if (!types[i].isSameType(targetTypes[i])) {
                    return false
                }
            }
            return true
        }
    }
    return false
}