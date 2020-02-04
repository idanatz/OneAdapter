package com.idanatz.oneadapter.tests.modules.empiness

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.Metadata
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

private const val NUM_OF_CLEARS = 3

@RunWith(AndroidJUnit4::class)
class WhenClearingItemsOnBindShouldBeCalledEachTime : BaseTest() {

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
            for(i in 0 until NUM_OF_CLEARS) {
                runWithDelay(i * 500L) { oneAdapter.clear() }
            }
        }

        // assertion
        waitUntilAsserted {
            onBindCalls shouldEqualTo NUM_OF_CLEARS
        }
    }

    inner class TestEmptinessModule : EmptinessModule() {
        override fun provideModuleConfig() = modulesGenerator.generateValidEmptinessModuleConfig(R.layout.test_model_small)
        override fun onBind(viewBinder: ViewBinder, metadata: Metadata) {
            onBindCalls++
        }
    }
}