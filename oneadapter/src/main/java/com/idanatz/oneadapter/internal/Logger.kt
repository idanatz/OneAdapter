package com.idanatz.oneadapter.internal

import android.util.Log
import com.idanatz.oneadapter.BuildConfig

object Logger {

    fun logd(tag: String = "OneAdapter", body: () -> String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, body())
        }
    }
}