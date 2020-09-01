package com.idanatz.oneadapter.tests.modules.empiness.metadata

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenOnBindInvokedItemPositionMetadataShouldBeZero : BaseTest() {

    private var position = -1

    @Test
    fun test() {
        configure {
            actOnActivity {
                oneAdapter.attachEmptinessModule(TestEmptinessModule())
            }
            untilAsserted {
                position shouldEqualTo 0
            }
        }
    }

    inner class TestEmptinessModule : EmptinessModule() {
        init {
            config = modulesGenerator.generateValidEmptinessModuleConfig()
            onBind { _, metadata ->
                position = metadata.position
            }
        }
    }
}