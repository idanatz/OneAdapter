package com.idanatz.oneadapter.tests.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.UnsupportedClassException
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.models.TestModel
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetItemViewTypeFromClassMissingModuleException : BaseTest() {

    @Test(expected = UnsupportedClassException::class)
    fun test() {
        configure {
            act {
                oneAdapter.getItemViewTypeFromClass(TestModel::class.java)
            }
        }
    }
}