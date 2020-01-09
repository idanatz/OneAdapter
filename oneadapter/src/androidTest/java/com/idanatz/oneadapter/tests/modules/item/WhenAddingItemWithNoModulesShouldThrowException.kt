package com.idanatz.oneadapter.tests.modules.item

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.external.MissingModuleDefinitionException
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenAddingItemWithNoModulesShouldThrowException : BaseTest() {

    @Test(expected = MissingModuleDefinitionException::class)
    fun test() {
        // action & assertion
        throw catchException {
            oneAdapter.add(modelGenerator.generateModel())
        }
    }
}