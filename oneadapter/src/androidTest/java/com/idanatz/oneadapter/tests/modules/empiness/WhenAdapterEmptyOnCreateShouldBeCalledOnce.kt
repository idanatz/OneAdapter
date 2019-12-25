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
class WhenAdapterEmptyOnCreateShouldBeCalledOnce : BaseTest() {

    private var onCreateCalls = 0

    @Test
    fun test() {
        // preparation & action
        runOnActivity {
            oneAdapter.attachEmptinessModule(TestEmptinessModule())
        }

        // assertion
        await().untilAsserted {
            onCreateCalls shouldEqualTo 1
        }
    }

    inner class TestEmptinessModule : EmptinessModule() {
        override fun provideModuleConfig() = modulesGenerator.generateValidEmptinessModuleConfig(R.layout.test_model_small)
        override fun onBind(viewBinder: ViewBinder) {}
        override fun onCreated(viewBinder: ViewBinder) {
            onCreateCalls++
        }
    }
}