package com.idanatz.sample.examples.binding

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class ButterKnifeActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter(recyclerView) {
            itemModules += MessageItemModule()
        }

        oneAdapter.setItems(modelGenerator.generateMessages(10))
    }

    // Note: a class must be declared in order for ButterKnife to work.
    // Do not use anonymous ItemModule class or your view will not be binded by ButterKnife.
    class MessageItemModule : ItemModule<MessageModel>() {

        @BindView(R.id.title) lateinit var title: TextView
        @BindView(R.id.body) lateinit var body: TextView
        @BindView(R.id.avatarImage) lateinit var image: ImageView

        init {
            config {
                layoutResource = R.layout.message_model
            }
            onCreate { viewBinder ->
                ButterKnife.bind(this, viewBinder.rootView)
            }
            onBind { model, viewBinder, _ ->
                title.text = model.title
                body.text = model.body
                Glide.with(viewBinder.rootView).load(model.avatarImageId).into(image)
            }
        }
    }
}