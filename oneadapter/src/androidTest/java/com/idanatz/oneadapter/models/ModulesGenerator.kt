package com.idanatz.oneadapter.models

import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.test.R

class ModulesGenerator {

    fun generateValidItemModule(): ItemModule<TestModel> = object : ItemModule<TestModel>() {
        override fun provideModuleConfig() = generateValidItemModuleConfig(R.layout.test_model_small)
        override fun onBind(model: TestModel, viewBinder: ViewBinder) {}
    }

    fun generateValidItemModuleConfig(resourceId: Int): ItemModuleConfig = object : ItemModuleConfig() {
        override fun withLayoutResource(): Int = resourceId
    }
}