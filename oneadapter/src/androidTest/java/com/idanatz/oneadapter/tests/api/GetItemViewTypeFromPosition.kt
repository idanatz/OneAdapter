package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.holders.EmptyIndicator
import com.idanatz.oneadapter.external.holders.LoadingIndicator
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.models.TestModel1
import com.idanatz.oneadapter.models.TestModel2
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetItemViewTypeFromPosition : BaseTest() {

    @Test
    fun test() {
        configure {
            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(TestItemModule1())
                    attachItemModule(TestItemModule2())
                    attachEmptinessModule(modulesGenerator.generateValidEmptinessModule())
                }
            }
            actOnActivity {
                runWithDelay(250) { oneAdapter.add(modelGenerator.generateDifferentModels(2)) }
            }
            untilAsserted {
                oneAdapter.getItemViewType(0) shouldEqualTo 0
                oneAdapter.getItemViewType(1) shouldEqualTo 1
                oneAdapter.getItemViewTypeFromClass(EmptyIndicator::class.java) shouldEqualTo 2
            }
        }
    }

    inner class TestItemModule1 : ItemModule<TestModel1>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_small)
        override fun onBind(item: Item<TestModel1>, viewBinder: ViewBinder) {}
    }

    inner class TestItemModule2 : ItemModule<TestModel2>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_small)
        override fun onBind(item: Item<TestModel2>, viewBinder: ViewBinder) {}
    }
}