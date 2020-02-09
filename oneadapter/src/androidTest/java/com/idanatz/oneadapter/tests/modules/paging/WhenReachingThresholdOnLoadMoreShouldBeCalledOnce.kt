package com.idanatz.oneadapter.tests.modules.paging

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.external.modules.PagingModuleConfig
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

private const val numberOfItemsToCreate = 10
private const val pagingVisibleThreshold = 3

@RunWith(AndroidJUnit4::class)
class WhenReachingThresholdOnLoadMoreShouldBeCalledOnce : BaseTest() {

    private var onLoadMoreCalls = 0

    @Test
    fun test() {
        configure {
            prepareOnActivity {
                oneAdapter.apply {
                    attachItemModule(modulesGenerator.generateValidItemModule(R.layout.test_model_large))
                    attachPagingModule(TestPagingModule())
                    oneAdapter.internalAdapter.data = modelGenerator.generateModels(numberOfItemsToCreate).toMutableList()
                }
            }
            actOnActivity {
                runWithDelay { // run with delay to let the items settle
                    val positionToScroll = numberOfItemsToCreate - pagingVisibleThreshold + 1 // + 1 for passing the threshold
                    recyclerView.smoothScrollToPosition(positionToScroll)
                }
            }
            untilAsserted {
                onLoadMoreCalls shouldEqualTo 1
            }
        }
    }

    inner class TestPagingModule : PagingModule() {
        override fun provideModuleConfig(): PagingModuleConfig = modulesGenerator.generateValidPagingModuleConfig(pagingVisibleThreshold)
        override fun onLoadMore(currentPage: Int) {
            onLoadMoreCalls++
        }
    }
}