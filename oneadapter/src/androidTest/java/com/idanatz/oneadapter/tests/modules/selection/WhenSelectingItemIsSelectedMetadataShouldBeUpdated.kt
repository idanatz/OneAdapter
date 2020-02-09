package com.idanatz.oneadapter.tests.modules.selection

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModuleConfig
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.helpers.getViewLocationOnScreen
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.test.R
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenSelectingItemIsSelectedMetadataShouldBeUpdated : BaseTest() {

	private var isSelectedCount = 0
	private var isNotSelectedCount = 0

	@Test
	fun test() {
		configure {
			prepareOnActivity {
				oneAdapter
						.attachItemModule(TestItemModule().addState(TestSelectionState()))
						.attachItemSelectionModule(TestItemSelectionModule())
			}
			actOnActivity {
				oneAdapter.add(modelGenerator.generateModel())
				runWithDelay {
					val holderRootView = recyclerView.findViewHolderForAdapterPosition(0)?.itemView
					holderRootView?.post {
						val (x, y) = holderRootView.getViewLocationOnScreen()

						touchSimulator.simulateLongTap(recyclerView, x, y)
						runWithDelay(100) { touchSimulator.simulateTap(recyclerView, x, y) }
					}
				}
			}
			untilAsserted {
				isSelectedCount shouldEqualTo 1
				isNotSelectedCount shouldEqualTo 1
			}
		}
	}

	inner class TestItemModule : ItemModule<TestModel>() {
		override fun provideModuleConfig() = modulesGenerator.generateValidItemModuleConfig(R.layout.test_model_large)
		override fun onBind(item: Item<TestModel>, viewBinder: ViewBinder) {
			if (item.metadata.isSelected) {
				viewBinder.rootView.setBackgroundColor(Color.parseColor("#C7226E"))
				isSelectedCount++
			} else {
				viewBinder.rootView.setBackgroundColor(Color.parseColor("#ffffff"))
				isNotSelectedCount++
			}
		}
	}

	private inner class TestSelectionState : SelectionState<TestModel>() {
        override fun isSelectionEnabled(model: TestModel): Boolean = true
    }

	private class TestItemSelectionModule : ItemSelectionModule() {
		override fun provideModuleConfig(): ItemSelectionModuleConfig = object : ItemSelectionModuleConfig() {
			override fun withSelectionType() = SelectionType.Single
		}
	}
}