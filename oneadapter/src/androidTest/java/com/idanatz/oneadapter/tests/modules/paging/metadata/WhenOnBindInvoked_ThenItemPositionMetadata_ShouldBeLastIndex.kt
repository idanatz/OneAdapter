@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.paging.metadata

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenOnBindInvoked_ThenItemPositionMetadata_ShouldBeLastIndex : BaseTest() {

    private var position = -1

	@Test
	fun test() {
		configure {
			prepareOnActivity {
				oneAdapter.run {
					attachItemModule(modulesGenerator.generateValidItemModule(R.layout.test_model_large))
					attachPagingModule(TestPagingModule())
					setItems(modelGenerator.generateModels(10).toMutableList())
				}
			}
			actOnActivity {
				runWithDelay { // run with delay to let the items settle
					recyclerView.smoothScrollToPosition(oneAdapter.itemCount)
				}
			}
			untilAsserted {
				position shouldEqualTo (oneAdapter.itemCount - 1)
			}
		}
	}

	inner class TestPagingModule : PagingModule() {
		init {
			config = modulesGenerator.generateValidPagingModuleConfig()
			onBind { _, metadata ->
				position = metadata.position
			}
		}
	}
}