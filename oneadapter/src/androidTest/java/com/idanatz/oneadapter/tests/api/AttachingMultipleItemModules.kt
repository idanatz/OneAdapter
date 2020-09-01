package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.models.TestModel1
import com.idanatz.oneadapter.models.TestModel2
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AttachingMultipleItemModules : BaseTest() {

    @Test
    fun test() {
        configure {
            val testItemModule1 = TestItemModule1()
            val testItemModule2 = TestItemModule2()

            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModules(testItemModule1, testItemModule2)
                }
            }
            untilAsserted {
                oneAdapter.modules.itemModules.size shouldEqualTo 2
                oneAdapter.modules.itemModules shouldContain Pair(TestModel1::class.java, testItemModule1)
                oneAdapter.modules.itemModules shouldContain Pair(TestModel2::class.java, testItemModule2)
            }
        }
    }

    inner class TestItemModule1 : ItemModule<TestModel1>() {
        init {
            config = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_small)
        }
    }

    inner class TestItemModule2 : ItemModule<TestModel2>() {
        init {
            config = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_small)
        }
    }
}