package com.idanatz.sample.examples.features

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.events.ClickEventHook
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class ClickEventHookActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(messageItem().addEventHook(clickEventHook()))

        oneAdapter.setItems(modelGenerator.generateFirstMessages())
    }

    private fun messageItem(): ItemModule<MessageModel> = object : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.message_model
        }

        override fun onBind(model: MessageModel, viewBinder: ViewBinder) {
            val title = viewBinder.findViewById<TextView>(R.id.title)
            val body = viewBinder.findViewById<TextView>(R.id.body)
            val image = viewBinder.findViewById<ImageView>(R.id.avatarImage)

            title.text = model.title
            body.text = model.body
            Glide.with(this@ClickEventHookActivity).load(model.avatarImageId).into(image)
        }
    }

    private fun clickEventHook(): ClickEventHook<MessageModel> = object : ClickEventHook<MessageModel>() {
        override fun onClick(model: MessageModel, viewBinder: ViewBinder) = Toast.makeText(this@ClickEventHookActivity, "${model.title} clicked", Toast.LENGTH_SHORT).show()
    }
}