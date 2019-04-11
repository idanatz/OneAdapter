package com.android.oneadapter.internal.holder_config

import android.support.annotation.LayoutRes
import com.android.oneadapter.utils.MissingBuilderArgumentException

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
            } ?: throw MissingBuilderArgumentException("HolderConfigBuilder Missing Layout Resource")
        }
    }
}