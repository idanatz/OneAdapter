@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotContain
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenCallingRemoveOnMultipleItemsWithModels_ThenInternalState_ShouldBeUpdated : BaseTest() {

    @Test
    fun test() {
        configure {
            val models = modelGenerator.generateModels(4)
            val modelsToRemove = models.subList(1, 3)
            var oldItemCount = -1

            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule())
                    internalAdapter.data = models.toMutableList()
                    oldItemCount = itemCount
                }
            }
            actOnActivity {
                oneAdapter.remove(modelsToRemove)
            }
            untilAsserted {
                val newItemList = oneAdapter.internalAdapter.data
                val newItemCount = oneAdapter.itemCount
                newItemList shouldNotContain modelsToRemove
                newItemCount shouldEqualTo (oldItemCount - modelsToRemove.size)
            }
        }
    }
}