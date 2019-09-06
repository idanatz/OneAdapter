package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddSingleItemToIndex : BaseTest() {

    companion object {
        const val INDEX = 3
    }

    @Test
    fun test() {
        // preparation
        val modelToAdd = modelGenerator.generateModel()
        var oldItemCount = -1
        runOnActivity {
            oneAdapter.apply {
                attachItemModule(modulesGenerator.generateValidItemModule())
                internalAdapter.data = modelGenerator.generateModels(5).toMutableList()
                oldItemCount = oneAdapter.itemCount
            }
        }

        // action
        runOnActivity {
            oneAdapter.add(INDEX, modelToAdd)
        }

        // assertion
        await().untilAsserted {
            val newItemList = oneAdapter.internalAdapter.data
            val newItemCount = oneAdapter.itemCount
            newItemList[INDEX] shouldEqual modelToAdd
            newItemCount shouldEqualTo (oldItemCount + 1)
        }
    }
}