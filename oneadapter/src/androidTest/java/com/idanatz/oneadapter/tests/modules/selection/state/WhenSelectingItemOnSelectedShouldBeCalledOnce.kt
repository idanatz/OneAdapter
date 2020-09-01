package com.idanatz.oneadapter.tests.modules.selection.state

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.helpers.getViewLocationOnScreen
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenSelectingItemOnSelectedShouldBeCalledOnce : BaseTest() {

	private var onSelectedCalls = 0
	private var isSelectedCount = 0
	private var isNotSelectedCount = 0

	@Test
	fun test() {
		configure {
			prepareOnActivity {
				oneAdapter.apply {
					attachItemModule(TestItemModule())
					attachItemSelectionModule(ItemSelectionModule())
					oneAdapter.internalAdapter.data = mutableListOf(modelGenerator.generateModel())
				}
			}
			actOnActivity {
				runWithDelay {
					val holderRootView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
					holderRootView?.post {
						val (x, y) = holderRootView.getViewLocationOnScreen()

						touchSimulator.simulateLongTouch(recyclerView, x, y)
						runWithDelay(100) { touchSimulator.simulateTouch(recyclerView, x, y) }
					}
				}
			}
			untilAsserted {
				onSelectedCalls shouldEqualTo 2
				isSelectedCount shouldEqualTo 1
				isNotSelectedCount shouldEqualTo 1
			}
		}
	}

	inner class TestItemModule : ItemModule<TestModel>() {
		init {
			config = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
			onBind { _, viewBinder, metadata ->
				if (metadata.isSelected) viewBinder.rootView.setBackgroundColor(Color.parseColor("#C7226E"))
				else viewBinder.rootView.setBackgroundColor(Color.parseColor("#ffffff"))
			}
			states += SelectionState<TestModel>().apply {
				onSelected { _, selected ->
					onSelectedCalls++
					if (selected) isSelectedCount++
					else isNotSelectedCount++
				}
			}
		}
	}
}