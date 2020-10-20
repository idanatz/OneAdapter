@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.selection.metadata

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.helpers.getViewLocationOnScreen
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenScrollingAfterSelectingItem_ThenIsSelectedMetadata_ShouldBeCorrect : BaseTest() {

	private var isSelectedCount = 0

	@Test
	fun test() {
		configure {
			prepareOnActivity {
				oneAdapter
						.attachItemModule(TestItemModule())
						.attachItemSelectionModule(ItemSelectionModule())
			}
			actOnActivity {
				oneAdapter.add(modelGenerator.generateModels(25)) // enough to cause a re-bind of the same model
				runWithDelay {
					val holderRootView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
					holderRootView?.post {
						val (x, y) = holderRootView.getViewLocationOnScreen()

						touchSimulator.simulateLongTouch(recyclerView, x, y)

						runWithDelay {
							recyclerView.smoothScrollToPosition(oneAdapter.itemCount)
						}
					}
				}
			}
			untilAsserted(assertDelay = 2000) {
				isSelectedCount shouldEqualTo 1
			}
		}
	}

	inner class TestItemModule : ItemModule<TestModel>() {
		init {
			config = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
			onBind { _, viewBinder, metadata ->
				if (metadata.isSelected) {
					viewBinder.rootView.setBackgroundColor(Color.parseColor("#C7226E"))
					isSelectedCount++
				}
			}
			states += SelectionState()
		}
	}
}