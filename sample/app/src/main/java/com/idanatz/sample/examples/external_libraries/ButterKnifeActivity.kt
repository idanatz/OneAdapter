package com.idanatz.sample.examples.external_libraries

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class ButterKnifeActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(MessageItemModule())

        oneAdapter.setItems(modelGenerator.generateFirstMessages())
    }

    // Note: a class must be declared in order for ButterKnife to work.
    // Do not use anonymous ItemModule class or your view will not be binded by ButterKnife.
    class MessageItemModule : ItemModule<MessageModel>() {

        @BindView(R.id.title) lateinit var title: TextView
        @BindView(R.id.body) lateinit var body: TextView
        @BindView(R.id.avatarImage) lateinit var image: ImageView

        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.message_model
        }

        override fun onCreated(viewBinder: ViewBinder) {
            ButterKnife.bind(this, viewBinder.getRootView())
        }

        override fun onBind(model: MessageModel, viewBinder: ViewBinder) {
            title.text = model.title
            body.text = model.body
            Glide.with(viewBinder.getRootView()).load(model.avatarImageId).into(image)
        }
    }
}