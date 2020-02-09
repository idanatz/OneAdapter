package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

private const val updatedValue = "updated"

@RunWith(AndroidJUnit4::class)
class UpdateSingleItem : BaseTest() {

    @Test
    fun test() {
        configure {
            val modelToUpdate = modelGenerator.generateModel()
            var oldItemCount = -1

            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(TestItemModule())
                    internalAdapter.data = mutableListOf(modelToUpdate)
                    oldItemCount = itemCount
                }
            }
            actOnActivity {
                modelToUpdate.content = updatedValue
                oneAdapter.update(modelToUpdate)
            }
            untilAsserted {
                val newItemCount = oneAdapter.itemCount
                val newContent = (oneAdapter.internalAdapter.data[0] as TestModel).content

                newContent shouldBeEqualTo updatedValue
                modelToUpdate.onBindCalls shouldEqualTo 1
                newItemCount shouldEqualTo oldItemCount
            }
        }
    }

    inner class TestItemModule : ItemModule<TestModel>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
        override fun onBind(item: Item<TestModel>, viewBinder: ViewBinder) {
            item.model.onBindCalls++
        }
    }
}