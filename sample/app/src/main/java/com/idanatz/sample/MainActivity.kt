package com.idanatz.sample

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.events.ClickEventHook
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.examples.complete.view.CompleteJavaExampleActivity
import com.idanatz.sample.examples.complete.view.CompleteKotlinExampleActivity
import com.idanatz.sample.examples.features.*
import com.idanatz.sample.models.ActivityModel

class MainActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter()
                .attachItemModule(activityItemModule())
                .attachTo(recyclerView)

        oneAdapter.setItems(listOf(
                ActivityModel(getString(R.string.single_item_module_example), Intent(this@MainActivity, SingleItemModuleActivity::class.java)),
                ActivityModel(getString(R.string.multiple_item_modules_example), Intent(this@MainActivity, MultipleItemModuleActivity::class.java)),
                ActivityModel(getString(R.string.emptiness_module_example), Intent(this@MainActivity, EmptinessModuleActivity::class.java)),
                ActivityModel(getString(R.string.paging_module_example), Intent(this@MainActivity, PagingModuleActivity::class.java)),
                ActivityModel(getString(R.string.item_selection_module_example), Intent(this@MainActivity, ItemSelectionModuleActivity::class.java)),
                ActivityModel(getString(R.string.click_event_hook_example), Intent(this@MainActivity, ClickEventHookActivity::class.java)),
                ActivityModel(getString(R.string.swipe_event_hook_example), Intent(this@MainActivity, SwipeEventHookActivity::class.java)),
                ActivityModel(getString(R.string.complete_kotlin_example), Intent(this@MainActivity, CompleteKotlinExampleActivity::class.java)),
                ActivityModel(getString(R.string.complete_java_example), Intent(this@MainActivity, CompleteJavaExampleActivity::class.java))
        ))
    }

    private fun activityItemModule(): ItemModule<ActivityModel> {
        return object : ItemModule<ActivityModel>() {
            override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
                override fun withLayoutResource() = R.layout.activity_model
            }

            override fun onBind(model: ActivityModel, viewBinder: ViewBinder) {
                viewBinder.findViewById<TextView>(R.id.title).text = model.text
            }
        }.addEventHook(object : ClickEventHook<ActivityModel>() {
            override fun onClick(model: ActivityModel, viewBinder: ViewBinder) = startActivity(model.intent)
        })
    }
}