package com.idanatz.oneadapter.tests.modules.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel1
import com.idanatz.oneadapter.models.TestModel2
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenAddingFewDifferentItemsOnBindShouldBeCalledOnceForEachItemOnScreen : BaseTest() {

    @Test
    fun test() {
        // preparation
        val testedLayoutResource = R.layout.test_model_large
        val numberOfHoldersInScreen = getNumberOfHoldersCanBeOnScreen(testedLayoutResource)
        val models = modelGenerator.generateDifferentModels(numberOfHoldersInScreen) // about 6 items
        val itemModule1 = object : ItemModule<TestModel1>() {
            override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
            override fun onBind(model: TestModel1, viewBinder: ViewBinder) {
                model.onBindCalls++
            }
        }
        val itemModule2 = object : ItemModule<TestModel2>() {
            override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
            override fun onBind(model: TestModel2, viewBinder: ViewBinder) {
                model.onBindCalls++
            }
        }
        runOnActivity {
            oneAdapter.attachItemModule(itemModule1)
            oneAdapter.attachItemModule(itemModule2)
        }

        // action
        runOnActivity {
            oneAdapter.add(models)
        }

        // assertion
        await().untilAsserted {
            models.sumBy { it.onBindCalls } shouldEqualTo numberOfHoldersInScreen
        }
    }
}