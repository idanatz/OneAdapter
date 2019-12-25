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
class WhenAddingFewDifferentItemsOnCreateShouldBeCalledOnceForEachItemOnScreen : BaseTest() {

    private val testedLayoutResource = R.layout.test_model_large
    private var onCreateCalls = 0

    @Test
    fun test() {
        // preparation
        val numberOfHoldersInScreen = getNumberOfHoldersThatCanBeOnScreen(testedLayoutResource)
        val models = modelGenerator.generateDifferentModels(numberOfHoldersInScreen) // about 6 items
        runOnActivity {
            oneAdapter.attachItemModule(TestItemModule1())
            oneAdapter.attachItemModule(TestItemModule2())
        }

        // action
        runOnActivity {
            oneAdapter.add(models)
        }

        // assertion
        await().untilAsserted {
            onCreateCalls shouldEqualTo numberOfHoldersInScreen
        }
    }

    inner class TestItemModule1 : ItemModule<TestModel1>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
        override fun onBind(model: TestModel1, viewBinder: ViewBinder) {}
        override fun onCreated(viewBinder: ViewBinder) {
            onCreateCalls++
        }
    }

    inner class TestItemModule2 : ItemModule<TestModel2>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
        override fun onBind(model: TestModel2, viewBinder: ViewBinder) {}
        override fun onCreated(viewBinder: ViewBinder) {
            onCreateCalls++
        }
    }
}