package com.android.oneadapter.internal.holder_config

import android.support.annotation.LayoutRes

open class HolderConfig<M> protected constructor(@LayoutRes var layoutResource: Int, var modelClass: Class<M>?) {

    class HolderConfigBuilder<M> {
        @LayoutRes private var layoutResource: Int? = null
        private var modelClass: Class<M>? = null

        fun withLayoutResource(@LayoutRes layoutResource: Int): HolderConfigBuilder<M> {
            this.layoutResource = layoutResource
            return this
        }

        fun withModelClass(modelClass: Class<M>): HolderConfigBuilder<M> {
            this.modelClass = modelClass
            return this
        }

        fun build(): HolderConfig<M> {
            layoutResource?.let { layoutResource ->
                return HolderConfig(layoutResource, modelClass)
            } ?: throw Throwable("layout resource must not be null")
        }
    }
}