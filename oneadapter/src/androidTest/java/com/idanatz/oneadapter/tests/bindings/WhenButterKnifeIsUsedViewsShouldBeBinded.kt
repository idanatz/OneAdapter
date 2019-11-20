package com.idanatz.oneadapter.tests.bindings

import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import butterknife.BindView
import butterknife.ButterKnife
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel1
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldNotBe
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenButterKnifeIsUsedViewsShouldBeBinded : BaseTest() {

    var bindedView: TextView? = null

    @Test
    fun test() {
        // preparation
        val models = modelGenerator.generateModel()
        val itemModule = TestItemModule()

        runOnActivity {
            oneAdapter.attachItemModule(itemModule)
        }

        // action
        runOnActivity {
            oneAdapter.add(models)
        }

        // assertion
        await().untilAsserted {
            bindedView shouldNotBe null
        }
    }

    inner class TestItemModule : ItemModule<TestModel1>() {
        @BindView(R.id.test_model_large) lateinit var text: TextView

        override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
        override fun onCreated(viewBinder: ViewBinder) {
            ButterKnife.bind(this, viewBinder.rootView)
        }
        override fun onBind(model: TestModel1, viewBinder: ViewBinder) {
            bindedView = text
        }
    }
}