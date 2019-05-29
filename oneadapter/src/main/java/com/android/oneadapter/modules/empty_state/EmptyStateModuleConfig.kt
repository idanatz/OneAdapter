package com.android.oneadapter.modules.empty_state

import androidx.annotation.LayoutRes
import com.android.oneadapter.modules.holder.HolderModuleConfig
import com.android.oneadapter.utils.MissingBuilderArgumentException

class EmptyStateModuleConfig private constructor(
        @LayoutRes layoutResource: Int
) : HolderModuleConfig<Any>(layoutResource, null) {

    class HolderConfigBuilder {
        @LayoutRes private var layoutResource: Int? = null

        fun withLayoutResource(@LayoutRes layoutResource: Int): HolderConfigBuilder {
            this.layoutResource = layoutResource
            return this
        }

        fun build(): EmptyStateModuleConfig {
            layoutResource?.let { layoutResource ->
                return EmptyStateModuleConfig(layoutResource)
            } ?: throw MissingBuilderArgumentException("HolderConfigBuilder missing layout resource")
        }
    }
}