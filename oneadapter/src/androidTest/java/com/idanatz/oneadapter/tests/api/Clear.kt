package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.awaitility.Awaitility
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Clear : BaseTest() {

    @Test
    fun test() {
        // preparation
        runOnActivity {
            oneAdapter.apply {
                attachItemModule(modulesGenerator.generateValidItemModule())
                internalAdapter.data = mutableListOf(modelGenerator.generateModel())
            }
        }

        // action
        runOnActivity {
            oneAdapter.clear()
        }

        // assertion
        waitUntilAsserted {
            val newItemList = oneAdapter.internalAdapter.data
            newItemList shouldHaveSize 0
        }
    }
}