package com.android.oneadapter.internal.holder_config

import android.support.annotation.LayoutRes

class LoadMoreHolderConfig private constructor(
        @LayoutRes layoutResource: Int,
        var visibleThreshold: Int
) : HolderConfig<Any>(layoutResource, null) {

    class HolderConfigBuilder {
        @LayoutRes private var layoutResource: Int? = null
        private var visibleThreshold: Int = 5

        fun withLayoutResource(@LayoutRes layoutResource: Int): HolderConfigBuilder {
            this.layoutResource = layoutResource
            return this
        }

        fun withVisibleThreshold(visibleThreshold: Int): HolderConfigBuilder {
            this.visibleThreshold = visibleThreshold
            return this
        }

        fun build(): LoadMoreHolderConfig {
            layoutResource?.let { layoutResource ->
                return LoadMoreHolderConfig(layoutResource, visibleThreshold)
            } ?: throw Throwable("layout resource must not be null")
        }
    }
}