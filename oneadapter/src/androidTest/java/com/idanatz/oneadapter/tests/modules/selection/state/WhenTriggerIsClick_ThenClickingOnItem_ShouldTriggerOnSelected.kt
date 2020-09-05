@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.modules.selection.state

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.external.states.SelectionStateConfig
import com.idanatz.oneadapter.helpers.getViewLocationOnScreen
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.models.TestModel
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenTriggerIsClick_ThenClickingOnItem_ShouldTriggerOnSelected : BaseTest() {

	private var onSelectedCalls = 0

	@Test
	fun test() {
		configure {
			prepareOnActivity {
				oneAdapter.apply {
					attachItemModule(modulesGenerator.generateValidItemModule().apply { states += TestSelectionState() })
					attachItemSelectionModule(ItemSelectionModule())
					oneAdapter.internalAdapter.data = mutableListOf(modelGenerator.generateModel())
				}
			}
			actOnActivity {
				runWithDelay {
					val holderRootView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
					holderRootView?.post {
						val (x, y) = holderRootView.getViewLocationOnScreen()

						touchSimulator.simulateTouch(recyclerView, x, y)
					}
				}
			}
			untilAsserted(assertDelay = 750) {
				onSelectedCalls shouldEqualTo 1
			}
		}
	}

	private inner class TestSelectionState : SelectionState<TestModel>() {
		init {
			config {
				selectionTrigger = SelectionStateConfig.SelectionTrigger.Click
			}
			onSelected { _, _ ->
				onSelectedCalls++
			}
		}
    }
}