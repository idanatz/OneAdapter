package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

private const val NUM_OF_ITEMS_TO_GENERATE = 20

@RunWith(AndroidJUnit4::class)
class SetItems : BaseTest() {

    @Test
    fun test() {
        configure {
            val modelsToSet = modelGenerator.generateModels(NUM_OF_ITEMS_TO_GENERATE)
            var oldItemCount = -1

            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule())
                    oldItemCount = itemCount
                }
            }
            actOnActivity {
                oneAdapter.setItems(modelsToSet)
            }
            untilAsserted {
                val newItemList = oneAdapter.internalAdapter.data
                val newItemCount = oneAdapter.itemCount
                newItemList shouldContainAll modelsToSet
                newItemCount shouldEqualTo (oldItemCount + NUM_OF_ITEMS_TO_GENERATE)
            }
        }
    }
}