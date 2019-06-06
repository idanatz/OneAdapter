package com.android.oneadapter.modules.holder

import androidx.annotation.LayoutRes
import com.android.oneadapter.utils.MissingBuilderArgumentException

open class HolderModuleConfig<M> protected constructor(
        @LayoutRes var layoutResource: Int,
        var selectionEnabled: Boolean = false
) {

    class Builder<M> {
        @LayoutRes private var layoutResource: Int? = null
        private var selectionEnabled = false

        fun withLayoutResource(@LayoutRes layoutResource: Int): Builder<M> {
            this.layoutResource = layoutResource
            return this
        }

        fun enableSelection(): Builder<M> {
            this.selectionEnabled = true
            return this
        }

        fun build(): HolderModuleConfig<M> {
            layoutResource?.let { layoutResource ->
                return HolderModuleConfig(layoutResource, selectionEnabled)
            } ?: throw MissingBuilderArgumentException("HolderConfigBuilder missing layout resource")
        }
    }
}