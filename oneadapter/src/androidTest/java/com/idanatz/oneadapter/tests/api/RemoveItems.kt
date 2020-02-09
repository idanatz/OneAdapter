package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotContain
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RemoveItems : BaseTest() {

    @Test
    fun test() {
        configure {
            val models = modelGenerator.generateModels(4)
            val modelToRemove = models.subList(1, 3)
            var oldItemCount = -1

            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule())
                    internalAdapter.data = models.toMutableList()
                    oldItemCount = itemCount
                }
            }
            actOnActivity {
                oneAdapter.remove(modelToRemove)
            }
            untilAsserted {
                val newItemList = oneAdapter.internalAdapter.data
                val newItemCount = oneAdapter.itemCount
                newItemList shouldNotContain modelToRemove
                newItemCount shouldEqualTo (oldItemCount - modelToRemove.size)
            }
        }
    }
}