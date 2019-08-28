package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqualTo
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddMultipleItems : BaseTest() {

    companion object {
        const val NUM_OF_ITEMS = 20
    }

    @Test
    fun test() {
        // preparation
        val modelsToAdd = modelGenerator.generateModels(NUM_OF_ITEMS)
        var oldItemCount = -1
        runOnActivity {
            oneAdapter.apply {
                attachItemModule(modulesGenerator.generateValidItemModule())
                oldItemCount = itemCount
            }
        }

        // action
        runOnActivity {
            oneAdapter.add(modelsToAdd)
        }

        // assertion
        await().untilAsserted {
            val newItemList = oneAdapter.internalAdapter.data
            val newItemCount = oneAdapter.itemCount
            newItemList shouldContainAll modelsToAdd
            newItemCount shouldEqualTo (oldItemCount + NUM_OF_ITEMS)
        }
    }
}