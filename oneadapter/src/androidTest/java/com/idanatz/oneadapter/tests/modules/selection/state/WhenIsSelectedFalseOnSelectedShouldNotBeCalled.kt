package com.idanatz.oneadapter.tests.modules.selection.state

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModuleConfig
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.helpers.getViewLocationOnScreen
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.models.TestModel
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenIsSelectedFalseOnSelectedShouldNotBeCalled : BaseTest() {

	private var onSelectedCalls = 0

	@Test
	fun test() {
		configure {
			prepareOnActivity {
				oneAdapter
						.attachItemModule(modulesGenerator.generateValidItemModule().addState(TestSelectionState()))
						.attachItemSelectionModule(TestItemSelectionModule())
			}
			actOnActivity {
				oneAdapter.add(modelGenerator.generateModel())
				runWithDelay {
					val holderRootView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
					holderRootView?.post {
						val (x, y) = holderRootView.getViewLocationOnScreen()

						touchSimulator.simulateLongTap(recyclerView, x, y)
					}
				}
			}
			untilAsserted(assertDelay = 750) {
				onSelectedCalls shouldEqualTo 0
			}
		}
	}

	private inner class TestSelectionState : SelectionState<TestModel>() {
        override fun isSelectionEnabled(model: TestModel): Boolean = false
        override fun onSelected(model: TestModel, selected: Boolean) {
	        onSelectedCalls++
        }
    }

	private class TestItemSelectionModule : ItemSelectionModule() {
		override fun provideModuleConfig(): ItemSelectionModuleConfig = object : ItemSelectionModuleConfig() {
			override fun withSelectionType() = SelectionType.Single
		}
	}
}