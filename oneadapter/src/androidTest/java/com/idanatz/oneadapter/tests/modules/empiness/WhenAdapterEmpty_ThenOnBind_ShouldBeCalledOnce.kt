@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.empiness

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenAdapterEmpty_ThenOnBind_ShouldBeCalledOnce : BaseTest() {

    private var onBindCalls = 0

    @Test
    fun test() {
        configure {
            actOnActivity {
                oneAdapter.attachEmptinessModule(TestEmptinessModule())
            }
            untilAsserted {
                onBindCalls shouldEqualTo 1
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