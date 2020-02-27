package com.idanatz.oneadapter.internal.utils

import android.util.Log
import com.idanatz.oneadapter.BuildConfig
import com.idanatz.oneadapter.internal.InternalAdapter

internal class Logger(val internalAdapter: InternalAdapter) {

    fun logd(body: () -> String) {
        if (BuildConfig.DEBUG) {
            Log.d("OneAdapter", "adapter: $internalAdapter, ${body()}")
        }
    }
}