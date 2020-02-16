package com.idanatz.oneadapter.tests.modules.empiness

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.holders.EmptyIndicator
import com.idanatz.oneadapter.external.interfaces.Item
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
        configure {
            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule())
                    attachEmptinessModule(TestEmptinessModule())
                    oneAdapter.internalAdapter.data = mutableListOf(modelGenerator.generateModel())
                }
            }
            actOnActivity {
                for(i in 0 until NUM_OF_CLEARS) {
                    runWithDelay(i * 500L) { oneAdapter.clear() }
                }
            }
            untilAsserted {
                onBindCalls shouldEqualTo NUM_OF_CLEARS
            }
        }
    }

    inner class TestEmptinessModule : EmptinessModule() {
        override fun provideModuleConfig() = modulesGenerator.generateValidEmptinessModuleConfig()
        override fun onBind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) {
            onBindCalls++
        }
    }
}