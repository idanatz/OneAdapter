package com.android.oneadapter.internal.holders

internal object LoadingIndicator {

    private const val TYPE_LOAD_MORE = -10

    fun getType() = TYPE_LOAD_MORE
}

internal object EmptyIndicator {

    private const val TYPE_EMPTY_STATE = -11

    fun getType() = TYPE_EMPTY_STATE
}