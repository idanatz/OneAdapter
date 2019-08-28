package com.idanatz.oneadapter.tests.modules.item_module

import android.os.Handler
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.awaitility.Awaitility
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OnUnbindShouldBeCalledOnceWhenItemLeavesTheScreen : BaseTest() {

    @Test
    fun test() {
        // preparation
        val testedLayoutResource = R.layout.test_model_large
        val numberOfHoldersInScreen = getNumberOfHoldersCanBeOnScreen(testedLayoutResource)
        val models = modelGenerator.generateModels(numberOfHoldersInScreen + 4) // create enough items that some can get recycled
        val itemModule = object : ItemModule<TestModel>() {
            override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
            override fun onBind(model: TestModel, viewBinder: ViewBinder) {}
            override fun onUnbind(model: TestModel, viewBinder: ViewBinder) {
                model.onUnbindCalls++
            }
        }
        runOnActivity {
            oneAdapter.attachItemModule(itemModule)
        }

        // action
        runOnActivity {
            oneAdapter.add(models)

            // run with delay to let the items settle
            Handler().postDelayed({
                oneAdapter.internalAdapter.recyclerView.scrollToPosition(oneAdapter.itemCount - 1)
            }, 750)
        }

        // assertion
        await().untilAsserted {
            models.sumBy { it.onUnbindCalls } shouldEqualTo 1
        }
    }
}