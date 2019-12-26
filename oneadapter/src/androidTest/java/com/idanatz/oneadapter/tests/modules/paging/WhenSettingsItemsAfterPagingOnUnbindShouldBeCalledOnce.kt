package com.idanatz.oneadapter.tests.modules.paging

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.external.modules.PagingModuleConfig
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.LoadingIndicator
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotContain
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenSettingsItemsAfterPagingOnUnbindShouldBeCalledOnce : BaseTest() {

    private var onUnbindCalls = 0

    @Test
    fun test() {
        // preparation
        runOnActivity {
            oneAdapter.apply {
                attachItemModule(modulesGenerator.generateValidItemModule(R.layout.test_model_large))
                attachPagingModule(TestPagingModule())
                oneAdapter.internalAdapter.data = modelGenerator.generateModels(10).toMutableList()
            }
        }

        // action
        runOnActivity {
            runWithDelay { // run with delay to let the items settle
                recyclerView.smoothScrollToPosition(oneAdapter.itemCount)
            }
        }

        // assertion
        await().untilAsserted {
            onUnbindCalls shouldEqualTo 1
            oneAdapter.internalAdapter.data shouldNotContain LoadingIndicator
        }
    }

    inner class TestPagingModule : PagingModule() {
        override fun provideModuleConfig(): PagingModuleConfig = modulesGenerator.generateValidPagingModuleConfig()
        override fun onLoadMore(currentPage: Int) {
            runWithDelay {
                // generate 10 other models
                oneAdapter.setItems(modelGenerator.generateModels(10))
            }
        }
        override fun onUnbind(viewBinder: ViewBinder) {
            onUnbindCalls++
        }
    }
}