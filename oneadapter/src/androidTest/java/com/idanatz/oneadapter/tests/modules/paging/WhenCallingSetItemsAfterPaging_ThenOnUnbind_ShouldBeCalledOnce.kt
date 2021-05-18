@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.paging

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.holders.LoadingIndicator
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldNotContain
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenCallingSetItemsAfterPaging_ThenOnUnbind_ShouldBeCalledOnce : BaseTest() {

    private var onUnbindCalls = 0

    @Test
    fun test() {
        configure {
            prepareOnActivity {
                oneAdapter.run {
                    attachItemModule(modulesGenerator.generateValidItemModule(R.layout.test_model_large))
                    attachPagingModule(TestPagingModule())
                    setItems(modelGenerator.generateModels(15).toMutableList())
                }
            }
            actOnActivity {
                runWithDelay { // run with delay to let the items settle
                    recyclerView.smoothScrollToPosition(oneAdapter.itemCount)
                }
            }
            untilAsserted {
                onUnbindCalls shouldEqualTo 1
                oneAdapter.internalAdapter.data shouldNotContain LoadingIndicator
            }
        }
    }

    inner class TestPagingModule : PagingModule() {
        init {
            config = modulesGenerator.generateValidPagingModuleConfig()
			onBind { _, _ ->
				// generate 2 other models
				oneAdapter.setItems(modelGenerator.generateModels(2))
			}
            onUnbind { _, _ ->
                onUnbindCalls++
            }
        }
    }
}