package com.android.oneadapter.internal

import android.util.Log
import com.android.oneadapter.BuildConfig

object Logger {

    fun logd(tag: String = "OneAdapter", body: () -> String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, body())
        }
    }
}