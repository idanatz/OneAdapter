package com.idanatz.oneadapter.tests.bindings

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import butterknife.BindView
import butterknife.ButterKnife
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.models.TestModel1
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldNotBe
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenButterKnifeIsUsedViewsShouldBeBinded : BaseTest() {

    private var bindedView: TextView? = null

    @Test
    fun test() {
        // preparation
        runOnActivity {
            oneAdapter.attachItemModule(TestItemModule())
        }

        // action
        runOnActivity {
            oneAdapter.add(modelGenerator.generateModel())
        }

        // assertion
        await().untilAsserted {
            bindedView shouldNotBe null
        }
    }

    inner class TestItemModule : ItemModule<TestModel>() {
        @BindView(R.id.test_model_large) lateinit var text: TextView

        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
        override fun onCreated(viewBinder: ViewBinder) {
            ButterKnife.bind(this, viewBinder.rootView)
        }
        override fun onBind(model: TestModel, viewBinder: ViewBinder) {
            bindedView = text
        }
    }
}