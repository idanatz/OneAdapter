package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldHaveSize
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Clear : BaseTest() {

    @Test
    fun test() {
        configure {
            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule())
                    internalAdapter.data = mutableListOf(modelGenerator.generateModel())
                }
            }
            actOnActivity {
                oneAdapter.clear()
            }
            untilAsserted {
                val newItemList = oneAdapter.internalAdapter.data
                newItemList shouldHaveSize 0
            }
        }
    }
}