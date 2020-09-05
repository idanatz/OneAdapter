@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.empiness

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.holders.EmptyIndicator
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotContain
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenAddingItem_ThenOnUnbind_ShouldBeCalledOnce : BaseTest() {

    private var onUnbindCalls = 0

    @Test
    fun test() {
        configure {
            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule())
                    attachEmptinessModule(TestEmptinessModule())
                }
            }
            actOnActivity {
                runWithDelay { // wait for the empty module onBind to get called
                    oneAdapter.add(modelGenerator.generateModel())
                }
            }
            untilAsserted {
                onUnbindCalls shouldEqualTo 1
                oneAdapter.internalAdapter.data shouldNotContain EmptyIndicator
            }
        }
    }

    inner class TestEmptinessModule : EmptinessModule() {
        init {
        	config = modulesGenerator.generateValidEmptinessModuleConfig()
            onUnbind { _, _ ->
                onUnbindCalls++
            }
        }
    }
}