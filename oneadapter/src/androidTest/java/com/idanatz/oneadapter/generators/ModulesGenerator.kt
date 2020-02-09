package com.idanatz.oneadapter.generators

import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.EmptinessModuleConfig
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.external.modules.PagingModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R

class ModulesGenerator {

    fun generateValidItemModule(resourceId: Int = R.layout.test_model_small): ItemModule<TestModel> = object : ItemModule<TestModel>() {
        override fun provideModuleConfig() = generateValidItemModuleConfig(resourceId)
        override fun onBind(item: Item<TestModel>, viewBinder: ViewBinder) {}
    }

    fun generateValidItemModuleConfig(resourceId: Int): ItemModuleConfig = object : ItemModuleConfig() {
        override fun withLayoutResource(): Int = resourceId
    }

    fun generateValidEmptinessModuleConfig(resourceId: Int): EmptinessModuleConfig = object : EmptinessModuleConfig() {
        override fun withLayoutResource(): Int = resourceId
    }

    fun generateValidPagingModuleConfig(visibleThreshold: Int = 3): PagingModuleConfig = object : PagingModuleConfig() {
        override fun withVisibleThreshold(): Int = visibleThreshold
        override fun withLayoutResource(): Int = R.layout.test_loading
    }
}