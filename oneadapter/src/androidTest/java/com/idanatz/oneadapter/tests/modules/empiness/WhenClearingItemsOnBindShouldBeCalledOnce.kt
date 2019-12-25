package com.idanatz.oneadapter.tests.modules.empiness

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenClearingItemsOnBindShouldBeCalledOnce : BaseTest() {

    private var onBindCalls = 0

    @Test
    fun test() {
        // preparation
        runOnActivity {
            oneAdapter.apply {
                attachItemModule(modulesGenerator.generateValidItemModule())
                attachEmptinessModule(TestEmptinessModule())
                oneAdapter.internalAdapter.data = mutableListOf(modelGenerator.generateModel())
            }
        }

        // action
        runOnActivity {
            oneAdapter.clear()
        }

        // assertion
        await().untilAsserted {
            onBindCalls shouldEqualTo 1
        }
    }

    inner class TestEmptinessModule : EmptinessModule() {
        override fun provideModuleConfig() = modulesGenerator.generateValidEmptinessModuleConfig(R.layout.test_model_small)
        override fun onBind(viewBinder: ViewBinder) {
            onBindCalls++
        }
    }
}