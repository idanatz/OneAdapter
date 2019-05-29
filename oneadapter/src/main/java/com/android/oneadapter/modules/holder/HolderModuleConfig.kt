package com.android.oneadapter.modules.holder

import androidx.annotation.LayoutRes
import com.android.oneadapter.utils.MissingBuilderArgumentException

open class HolderModuleConfig<M> protected constructor(
        @LayoutRes var layoutResource: Int,
        var modelClass: Class<M>?,
        var selectionEnabled: Boolean = false
) {

    class HolderConfigBuilder<M> {
        @LayoutRes private var layoutResource: Int? = null
        private var modelClass: Class<M>? = null
        private var selectionEnabled = false

        fun withLayoutResource(@LayoutRes layoutResource: Int): HolderConfigBuilder<M> {
            this.layoutResource = layoutResource
            return this
        }

        fun withModelClass(modelClass: Class<M>): HolderConfigBuilder<M> {
            this.modelClass = modelClass
            return this
        }

        fun enableSelection(): HolderConfigBuilder<M> {
            this.selectionEnabled = true
            return this
        }

        fun build(): HolderModuleConfig<M> {
            layoutResource?.let { layoutResource ->
                return HolderModuleConfig(layoutResource, modelClass, selectionEnabled)
            } ?: throw MissingBuilderArgumentException("HolderConfigBuilder missing layout resource")
        }
    }
}