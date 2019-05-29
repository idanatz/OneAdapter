package com.android.oneadapter.internal.holders

class LoadingIndicator {

    companion object {
        private const val TYPE_LOAD_MORE = -10

        fun getType() = TYPE_LOAD_MORE
    }
}

class EmptyIndicator {

    companion object {
        private const val TYPE_EMPTY_STATE = -11

        fun getType() = TYPE_EMPTY_STATE
    }
}