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
        // exception will be thrown from the RecyclerView thread so we need to catch it
        // and throw it again to the test thread
        throw catchException {
            oneAdapter.add(modelGenerator.generateModel())
        }
    }
}