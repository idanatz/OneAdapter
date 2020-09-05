@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.module_configs.selection

import android.graphics.Color
import android.util.SparseArray
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModuleConfig
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.helpers.getViewLocationOnScreen
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.utils.extensions.let2
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

private const val IS_SELECTED_COUNT_INDEX = 0
private const val IS_NOT_SELECTED_COUNT_INDEX = 1

@RunWith(AndroidJUnit4::class)
class WhenSelectionTypeSingle_ThenSelectingMultipleItems_ShouldNotBeSupported : BaseTest() {

	private val modelSelectedEvents = SparseArray<IntArray>(2)

	@Test
	fun test() {
		configure {
			val models = modelGenerator.generateModels(3)
			modelSelectedEvents.put(models[0].id, IntArray(2))
			modelSelectedEvents.put(models[1].id, IntArray(2))

			prepareOnActivity {
				oneAdapter.apply {
					attachItemModule(TestItemModule())
					attachItemSelectionModule(TestItemSelectionModule())
					oneAdapter.internalAdapter.data = models.toMutableList()
				}
			}
			actOnActivity {
				runWithDelay {
					val firstHolderRootView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
					val secondsHolderRootView = recyclerView.findViewHolderForAdapterPosition(1)?.itemView

					let2(firstHolderRootView, secondsHolderRootView) { rootView1: View, rootView2: View ->
						rootView1.post {
							val (x1, y1) = rootView1.getViewLocationOnScreen()
							touchSimulator.simulateLongTouch(recyclerView, x1, y1)

							runWithDelay(100) {
								val (x2, y2) = rootView2.getViewLocationOnScreen()
								touchSimulator.simulateTouch(recyclerView, x2, y2)
							}
						}
					}
				}
			}
			untilAsserted {
				modelSelectedEvents.get(models[0].id)[IS_SELECTED_COUNT_INDEX] shouldEqualTo 1
				modelSelectedEvents.get(models[0].id)[IS_NOT_SELECTED_COUNT_INDEX] shouldEqualTo 1
				modelSelectedEvents.get(models[1].id)[IS_SELECTED_COUNT_INDEX] shouldEqualTo 1
			}
		}
	}

	inner class TestItemModule : ItemModule<TestModel>() {
		init {
			config = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
			onBind { _, viewBinder, metadata ->
				if (metadata.isSelected) {
					viewBinder.rootView.setBackgroundColor(Color.parseColor("#C7226E"))
				} else {
					viewBinder.rootView.setBackgroundColor(Color.parseColor("#ffffff"))
				}
			}
			states += SelectionState<TestModel>().apply {
				onSelected { model, selected ->
					if (selected) { modelSelectedEvents.get(model.id)[IS_SELECTED_COUNT_INDEX]++ }
					else { modelSelectedEvents.get(model.id)[IS_NOT_SELECTED_COUNT_INDEX]++ }
				}
			}
		}
	}

	private class TestItemSelectionModule : ItemSelectionModule() {
		init {
			config {
				selectionType = ItemSelectionModuleConfig.SelectionType.Single
			}
		}
	}
}