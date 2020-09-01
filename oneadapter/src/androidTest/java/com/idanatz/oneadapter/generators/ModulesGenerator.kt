package com.idanatz.oneadapter.generators

import android.animation.Animator
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R

class ModulesGenerator {

    fun generateValidItemModule(resourceId: Int = R.layout.test_model_small) = object : ItemModule<TestModel>() {
        init {
            config = generateValidItemModuleConfig(resourceId)
        }
    }

    fun generateValidEmptinessModule() = object : EmptinessModule() {
        init {
            config = generateValidEmptinessModuleConfig()
        }
    }

    fun generateValidPagingModule() = object : PagingModule() {
        init {
            config = generateValidPagingModuleConfig()
        }
    }

    fun generateValidItemModuleConfig(resourceId: Int): ItemModuleConfig = object : ItemModuleConfig {
        override var layoutResource: Int? = resourceId
        override var firstBindAnimation: Animator? = null
    }

    fun generateValidEmptinessModuleConfig(resourceId: Int = R.layout.test_empty): EmptinessModuleConfig = object : EmptinessModuleConfig {
        override var layoutResource: Int? = resourceId
        override var firstBindAnimation: Animator? = null
    }

    fun generateValidPagingModuleConfig(visibleThreshold: Int = 3): PagingModuleConfig = object : PagingModuleConfig {
        override var layoutResource: Int? = R.layout.test_loading
        override var firstBindAnimation: Animator? = null
        override var visibleThreshold: Int = visibleThreshold
    }
}