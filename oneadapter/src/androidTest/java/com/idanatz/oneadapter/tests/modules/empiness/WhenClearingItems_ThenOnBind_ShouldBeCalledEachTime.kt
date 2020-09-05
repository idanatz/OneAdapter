@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.empiness

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

private const val NUM_OF_CLEARS = 3

@RunWith(AndroidJUnit4::class)
class WhenClearingItems_ThenOnBind_ShouldBeCalledEachTime : BaseTest() {

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
        init {
        	config = modulesGenerator.generateValidEmptinessModuleConfig()
            onBind { _, _ ->
                onBindCalls++
            }
        }
    }
}