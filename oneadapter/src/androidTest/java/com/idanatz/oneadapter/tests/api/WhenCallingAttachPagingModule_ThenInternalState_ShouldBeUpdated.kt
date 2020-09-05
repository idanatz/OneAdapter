@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import org.amshove.kluent.shouldEqual
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenCallingAttachPagingModule_ThenInternalState_ShouldBeUpdated : BaseTest() {

    @Test
    fun test() {
        configure {
            val testPagingModule = modulesGenerator.generateValidPagingModule()

            prepareOnActivity {
                oneAdapter.apply {
                    attachPagingModule(testPagingModule)
                }
            }
            untilAsserted {
                oneAdapter.modules.pagingModule shouldEqual testPagingModule
            }
        }
    }
}