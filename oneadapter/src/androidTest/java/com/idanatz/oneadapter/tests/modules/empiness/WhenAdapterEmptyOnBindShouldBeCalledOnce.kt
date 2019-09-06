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
class WhenAdapterEmptyOnBindShouldBeCalledOnce : BaseTest() {

    @Test
    fun test() {
        // preparation
        var onBindCalls = 0
        val emptinessModule = object : EmptinessModule() {
            override fun provideModuleConfig() = modulesGenerator.generateValidEmptinessModuleConfig(R.layout.test_model_small)
            override fun onBind(viewBinder: ViewBinder) {
                onBindCalls++
            }
        }

        // action
        runOnActivity {
            oneAdapter.attachEmptinessModule(emptinessModule)
        }

        // assertion
        await().untilAsserted {
            onBindCalls shouldEqualTo 1
        }
    }
}