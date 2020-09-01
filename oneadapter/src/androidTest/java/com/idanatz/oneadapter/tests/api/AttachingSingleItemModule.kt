package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.models.TestModel
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AttachingSingleItemModule : BaseTest() {

    @Test
    fun test() {
        configure {
            val testItemModule = modulesGenerator.generateValidItemModule()

            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(testItemModule)
                }
            }
            untilAsserted {
                oneAdapter.modules.itemModules.size shouldEqualTo 1
                oneAdapter.modules.itemModules shouldContain Pair(TestModel::class.java, testItemModule)
            }
        }
    }
}