package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

private const val UPDATED_VALUE_1 = "updated1"
private const val UPDATED_VALUE_2 = "updated2"

@RunWith(AndroidJUnit4::class)
class UpdateMultipleItemsByModel : BaseTest() {

    @Test
    fun test() {
        configure {
            val modelsToUpdate = modelGenerator.generateModels(2)
            var oldItemCount = -1

            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(TestItemModule())
                    internalAdapter.data = modelsToUpdate.toMutableList()
                    oldItemCount = itemCount
                }
            }
            actOnActivity {
                modelsToUpdate[0].content = UPDATED_VALUE_1
                modelsToUpdate[1].content = UPDATED_VALUE_2
                oneAdapter.update(modelsToUpdate)
            }
            untilAsserted {
                val newItemCount = oneAdapter.itemCount
                val model1 = (oneAdapter.internalAdapter.data[0] as TestModel)
                val model2 = (oneAdapter.internalAdapter.data[1] as TestModel)

                model1.content shouldBeEqualTo UPDATED_VALUE_1
                model1.onBindCalls shouldEqualTo 1
                model2.content shouldBeEqualTo UPDATED_VALUE_2
                model2.onBindCalls shouldEqualTo 1
                newItemCount shouldEqualTo oldItemCount
            }
        }
    }

    inner class TestItemModule : ItemModule<TestModel>() {
        init {
        	config = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
            onBind { model, _, _ ->
                model.onBindCalls++
            }
        }
    }
}