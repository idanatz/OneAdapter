@file:Suppress("ClassName")

package com.idanatz.oneadapter.tests.hooks.click_hook

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.helpers.BaseTest
import com.idanatz.oneadapter.helpers.getViewLocationOnScreen
import com.idanatz.oneadapter.models.TestModel
import org.amshove.kluent.shouldEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhenClickingOnAnItem_ThenOnClick_ShouldBeInvokedOnce : BaseTest() {

	private var onClickCalls = 0

	@Test
	fun test() {
		configure {
			prepareOnActivity {
				oneAdapter.attachItemModule(modulesGenerator.generateValidItemModule().apply { eventHooks += TestClickEventHook() })
				oneAdapter.internalAdapter.data = mutableListOf(modelGenerator.generateModel())
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
			untilAsserted {
				onClickCalls shouldEqualTo 1
			}
		}
	}

	inner class TestClickEventHook : ClickEventHook<TestModel>() {
		init {
			onClick { _, _, _ ->
				onClickCalls++
			}
		}
	}
}