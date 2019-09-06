package com.idanatz.sample.examples.features

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.models.HeaderModel

class MultipleItemModuleActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(messageItem())
                .attachItemModule(headerItem())

        val items = modelGenerator.addHeadersFromMessages(
                messages = modelGenerator.generateFirstMessages(),
                checkable = false
        )
        oneAdapter.setItems(items)
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
            Glide.with(this@MultipleItemModuleActivity).load(model.avatarImageId).into(image)
        }
    }

    private fun headerItem(): ItemModule<HeaderModel> = object : ItemModule<HeaderModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource() = R.layout.header_model
        }

        override fun onBind(model: HeaderModel, viewBinder: ViewBinder) {
            viewBinder.findViewById<TextView>(R.id.header_title).text = model.name
        }
    }
}