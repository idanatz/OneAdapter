package com.idanatz.oneadapter.tests.module_configs.layout_module_config

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.external.MissingConfigArgumentException
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.models.TestModel
import org.junit.Test
import org.junit.runner.RunWith

private const val INVALID_RESOURCE = -1

@RunWith(AndroidJUnit4::class)
class InvalidLayoutResourceShouldThrowException : BaseTest() {

    @Test(expected = MissingConfigArgumentException::class)
    fun test() {
        configure {
            act {
                oneAdapter.attachItemModule(TestItemModule())
            }
        }
    }

    inner class TestItemModule : ItemModule<TestModel>() {
        override fun provideModuleConfig() = object : ItemModuleConfig() {
            override fun withLayoutResource() = INVALID_RESOURCE
        }
        override fun onBind(item: Item<TestModel>, viewBinder: ViewBinder) {}
    }
}