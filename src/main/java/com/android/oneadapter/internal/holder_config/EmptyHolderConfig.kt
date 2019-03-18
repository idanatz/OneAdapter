package com.android.oneadapter.internal.holder_config

import android.support.annotation.LayoutRes

class EmptyHolderConfig private constructor(
        @LayoutRes layoutResource: Int
) : HolderConfig<Any>(layoutResource, null) {

    class HolderConfigBuilder {
        @LayoutRes private var layoutResource: Int? = null

        fun withLayoutResource(@LayoutRes layoutResource: Int): HolderConfigBuilder {
            this.layoutResource = layoutResource
            return this
        }

        fun build(): EmptyHolderConfig {
            layoutResource?.let { layoutResource ->
                return EmptyHolderConfig(layoutResource)
            } ?: throw Throwable("layout resource must not be null")
        }
    }
}