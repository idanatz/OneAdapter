package com.idanatz.oneadapter.tests.modules.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenAddingManyItemsOnBindShouldBeCalledOnceForEachItemOnScreen : BaseTest() {

    private val testedLayoutResource = R.layout.test_model_small

    @Test
    fun test() {
        // preparation
        val numberOfHoldersInScreen = getNumberOfHoldersThatCanBeOnScreen(testedLayoutResource)
        val models = modelGenerator.generateModels(numberOfHoldersInScreen) // about 70 items
        val itemModule = TestItemModule()
        runOnActivity {
            oneAdapter.attachItemModule(itemModule)
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

    inner class TestItemModule : ItemModule<TestModel>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
        override fun onBind(model: TestModel, viewBinder: ViewBinder) {
            model.onBindCalls++
        }
    }
}