@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.paging

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenEmptinessModuleAttached_ThenPaging_ShouldNotBeTriggered : BaseTest() {

    private var onBindCalls = 0
    private var onLoadMoreCalls = 0

    @Test
    fun test() {
        configure {
            prepareOnActivity {
                oneAdapter.run {
                    attachEmptinessModule(TestEmptinessModule())
                    attachPagingModule(TestPagingModule())
                }
            }
            actOnActivity {
                runWithDelay { // run with delay to let the items settle
                    recyclerView.smoothScrollToPosition(oneAdapter.itemCount)
                }
            }
            untilAsserted(assertDelay = 800) {
				onBindCalls shouldEqualTo 0
                onLoadMoreCalls shouldEqualTo 0
            }
        }
    }

	inner class TestEmptinessModule : EmptinessModule() {
		init {
			config = modulesGenerator.generateValidEmptinessModuleConfig(resourceId = R.layout.test_big_empty)
		}
	}

    inner class TestPagingModule : PagingModule() {
        init {
        	config = modulesGenerator.generateValidPagingModuleConfig()
			onBind { _, _ ->
				onBindCalls++
			}
			onLoadMore {
				onLoadMoreCalls++
			}
        }
    }
}