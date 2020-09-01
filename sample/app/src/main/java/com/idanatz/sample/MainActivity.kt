package com.idanatz.sample

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.examples.complete.CompleteExampleActivity
import com.idanatz.sample.examples.binding.ButterKnifeActivity
import com.idanatz.sample.examples.binding.data_binding.DataBindingActivity
import com.idanatz.sample.examples.features.*
import com.idanatz.sample.models.ActivityModel
import com.idanatz.sample.models.HeaderModel

class MainActivity : BaseExampleActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val oneAdapter = OneAdapter(recyclerView) {
			itemModules += HeaderItem()
			itemModules += ActivityItem()
		}

		oneAdapter.setItems(listOf(
				HeaderModel(0, "Features Examples"),
				ActivityModel(getString(R.string.single_item_module_example), Intent(this@MainActivity, SingleItemModuleActivity::class.java)),
				ActivityModel(getString(R.string.multiple_item_modules_example), Intent(this@MainActivity, MultipleItemModuleActivity::class.java)),
				ActivityModel(getString(R.string.emptiness_module_example), Intent(this@MainActivity, EmptinessModuleActivity::class.java)),
				ActivityModel(getString(R.string.paging_module_example), Intent(this@MainActivity, PagingModuleActivity::class.java)),
				ActivityModel(getString(R.string.item_selection_module_example), Intent(this@MainActivity, ItemSelectionModuleActivity::class.java)),
				ActivityModel(getString(R.string.click_event_hook_example), Intent(this@MainActivity, ClickEventHookActivity::class.java)),
				ActivityModel(getString(R.string.swipe_event_hook_example), Intent(this@MainActivity, SwipeEventHookActivity::class.java)),
				ActivityModel(getString(R.string.first_bind_animation_example), Intent(this@MainActivity, FirstBindAnimationActivity::class.java)),
				HeaderModel(1, "Binding Example"),
				ActivityModel(getString(R.string.butterknife_example), Intent(this@MainActivity, ButterKnifeActivity::class.java)),
				ActivityModel(getString(R.string.data_binding_example), Intent(this@MainActivity, DataBindingActivity::class.java)),
				HeaderModel(2, "Complete Example"),
				ActivityModel(getString(R.string.complete_example), Intent(this@MainActivity, CompleteExampleActivity::class.java))
		))
	}

	class HeaderItem : ItemModule<HeaderModel>() {
		init {
			config {
				layoutResource = R.layout.header_model
			}
			onBind { model, viewBinder, _ ->
				viewBinder.findViewById<TextView>(R.id.header_title).text = model.name
			}
		}
	}

	inner class ActivityItem : ItemModule<ActivityModel>() {
		init {
			config {
				layoutResource = R.layout.activity_model
			}
			onBind { model, viewBinder, _ ->
				viewBinder.findViewById<TextView>(R.id.title).text = model.text
			}
			eventHooks += ClickEventHook<ActivityModel>().apply {
				onClick { model, _, _ -> startActivity(model.intent) }
			}
		}
	}
}