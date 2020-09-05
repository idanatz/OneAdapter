@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.external.MultipleModuleConflictException
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenAttachingItemModuleWithExistingModel_ThenAttachItemModule_ShouldThrowException : BaseTest() {

    @Test(expected = MultipleModuleConflictException::class)
    fun test() {
        // action & assertion
        throw catchException {
            oneAdapter.apply {
                attachItemModule(modulesGenerator.generateValidItemModule())
                attachItemModule(modulesGenerator.generateValidItemModule())
            }
        }
    }
}