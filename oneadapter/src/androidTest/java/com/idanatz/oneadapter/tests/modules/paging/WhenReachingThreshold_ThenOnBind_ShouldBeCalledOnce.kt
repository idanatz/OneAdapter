@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.paging

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenReachingThreshold_ThenOnBind_ShouldBeCalledOnce : BaseTest() {

    private var onBindCalls = 0

    @Test
    fun test() {
        configure {
            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule(R.layout.test_model_large))
                    attachPagingModule(TestPagingModule())
                    oneAdapter.internalAdapter.data = modelGenerator.generateModels(10).toMutableList()
                }
            }
            actOnActivity {
                runWithDelay { // run with delay to let the items settle
                    recyclerView.smoothScrollToPosition(oneAdapter.itemCount)
                }
            }
            untilAsserted {
                onBindCalls shouldEqualTo 1
            }
        }
    }

    inner class TestPagingModule : PagingModule() {
        init {
        	config = modulesGenerator.generateValidPagingModuleConfig()
            onBind { _, _ ->
                onBindCalls++
            }
        }
    }
}