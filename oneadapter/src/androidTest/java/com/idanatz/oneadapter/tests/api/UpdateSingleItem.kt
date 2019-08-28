package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UpdateSingleItem : BaseTest() {

    @Test
    fun test() {
        // preparation
        val modelToUpdate = modelGenerator.generateModel()
        var oldItemCount = -1
        val itemModule = object : ItemModule<TestModel>() {
            override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
            override fun onBind(model: TestModel, viewBinder: ViewBinder) {
                model.onBindCalls++
            }
        }
        runOnActivity {
            oneAdapter.apply {
                attachItemModule(itemModule)
                internalAdapter.data = mutableListOf(modelToUpdate)
                oldItemCount = itemCount
            }
        }

        // action
        runOnActivity {
            modelToUpdate.content = "updated"
            oneAdapter.update(modelToUpdate)
        }

        // assertion
        await().untilAsserted {
            val newItemCount = oneAdapter.itemCount
            val newContent = (oneAdapter.internalAdapter.data[0] as TestModel).content

            newContent shouldBeEqualTo "updated"
            modelToUpdate.onBindCalls shouldEqualTo 1
            newItemCount shouldEqualTo oldItemCount
        }
    }
}