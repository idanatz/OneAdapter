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
import org.amshove.kluent.shouldNotContain
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenAddingItemOnUnbindShouldBeCalledOnce : BaseTest() {

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
        override fun provideModuleConfig() = modulesGenerator.generateValidEmptinessModuleConfig(R.layout.test_empty)
        override fun onUnbind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) {
            onUnbindCalls++
        }
    }
}