package com.android.oneadapter.modules.load_more

import androidx.annotation.LayoutRes
import com.android.oneadapter.modules.holder.HolderModuleConfig
import com.android.oneadapter.utils.MissingBuilderArgumentException

class LoadMoreModuleConfig private constructor(
        @LayoutRes layoutResource: Int,
        var visibleThreshold: Int
) : HolderModuleConfig<Any>(layoutResource, null) {

    class Builder {
        @LayoutRes private var layoutResource: Int? = null
        private var visibleThreshold: Int = 5

        fun withLayoutResource(@LayoutRes layoutResource: Int): Builder {
            this.layoutResource = layoutResource
            return this
        }

        fun withVisibleThreshold(visibleThreshold: Int): Builder {
            this.visibleThreshold = visibleThreshold
            return this
        }

        fun build(): LoadMoreModuleConfig {
            layoutResource?.let { layoutResource ->
                return LoadMoreModuleConfig(layoutResource, visibleThreshold)
            } ?: throw MissingBuilderArgumentException("HolderConfigBuilder missing layout resource")
        }
    }
}