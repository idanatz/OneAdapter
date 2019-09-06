package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqualTo
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddSingleItem : BaseTest() {

    @Test
    fun test() {
        // preparation
        val modelToAdd = modelGenerator.generateModel()
        var oldItemCount = -1
        runOnActivity {
            oneAdapter.apply {
                attachItemModule(modulesGenerator.generateValidItemModule())
                oldItemCount = itemCount
            }
        }

        // action
        runOnActivity {
            oneAdapter.add(modelToAdd)
        }

        // assertion
        await().untilAsserted {
            val newItemList = oneAdapter.internalAdapter.data
            val newItemCount = oneAdapter.itemCount
            newItemList shouldContain modelToAdd
            newItemCount shouldEqualTo (oldItemCount + 1)
        }
    }
}