package com.idanatz.oneadapter.tests.module_configs.internal_module_config

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.internal.utils.extensions.inflateLayout
import com.idanatz.oneadapter.models.TestModel1
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqual
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidLayoutResourceShouldInflateSuccessfully : BaseTest() {

    @Test
    fun test() {
        // preparation
        val testedLayoutResource = R.layout.test_model_small
        var rootView: View? = null
        var expectedLayoutId = 0
        val itemModule = object : ItemModule<TestModel1>() {
            override fun provideModuleConfig() = object : ItemModuleConfig() {
                override fun withLayoutResource() = testedLayoutResource
            }
            override fun onBind(model: TestModel1, viewBinder: ViewBinder) {
                rootView = viewBinder.getRootView()
            }
        }
        runOnActivity {
            oneAdapter.attachItemModule(itemModule)
            expectedLayoutId = it.inflateLayout(testedLayoutResource).id
        }

        // action
        runOnActivity {
            oneAdapter.add(modelGenerator.generateModel())
        }

        // assertion
        await().untilAsserted {
            expectedLayoutId shouldEqual rootView?.id
        }
    }
}