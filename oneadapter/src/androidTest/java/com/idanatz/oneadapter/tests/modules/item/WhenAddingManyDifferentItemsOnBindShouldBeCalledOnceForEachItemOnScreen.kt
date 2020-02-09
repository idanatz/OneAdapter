package com.idanatz.oneadapter.tests.modules.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel1
import com.idanatz.oneadapter.models.TestModel2
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenAddingManyDifferentItemsOnBindShouldBeCalledOnceForEachItemOnScreen : BaseTest() {

    private val testedLayoutResource = R.layout.test_model_small

    @Test
    fun test() {
        configure {
            val numberOfHoldersInScreen = getNumberOfHoldersThatCanBeOnScreen(testedLayoutResource)
            val models = modelGenerator.generateDifferentModels(numberOfHoldersInScreen) // about 70 items

            prepareOnActivity {
                oneAdapter.attachItemModule(TestItemModule1())
                oneAdapter.attachItemModule(TestItemModule2())
            }
            actOnActivity {
                oneAdapter.add(models)
            }
            untilAsserted {
                models.sumBy { it.onBindCalls } shouldEqualTo numberOfHoldersInScreen
            }
        }
    }

    inner class TestItemModule1 : ItemModule<TestModel1>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
        override fun onBind(item: Item<TestModel1>, viewBinder: ViewBinder) {
            item.model.onBindCalls++
        }
    }

    inner class TestItemModule2 : ItemModule<TestModel2>() {
        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(testedLayoutResource)
        override fun onBind(item: Item<TestModel2>, viewBinder: ViewBinder) {
            item.model.onBindCalls++
        }
    }
}