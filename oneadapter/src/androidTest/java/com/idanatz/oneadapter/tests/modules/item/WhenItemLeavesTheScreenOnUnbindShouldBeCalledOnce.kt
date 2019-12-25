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
class WhenItemLeavesTheScreenOnUnbindShouldBeCalledOnce : BaseTest() {

    private val testedLayoutResource = R.layout.test_model_large

    @Test
    fun test() {
        // preparation
        val numberOfHoldersInScreen = getNumberOfHoldersThatCanBeOnScreen(testedLayoutResource)
        val models = modelGenerator.generateModels(numberOfHoldersInScreen + 4) // create enough items that some can get recycled
        runOnActivity {
            oneAdapter.attachItemModule(TestItemModule())
            oneAdapter.add(models)
        }

        // action
        runOnActivity {
            runWithDelay { // run with delay to let the items settle
                recyclerView.smoothScrollToPosition(oneAdapter.itemCount - 1)
            }
        }

        // assertion
        await().untilAsserted {
            models.sumBy { it.onUnbindCalls } shouldEqualTo 1
        }
    }

    inner class TestItemModule : ItemModule<TestModel>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
        override fun onBind(model: TestModel, viewBinder: ViewBinder) {}
        override fun onUnbind(model: TestModel, viewBinder: ViewBinder) {
            model.onUnbindCalls++
        }
    }
}