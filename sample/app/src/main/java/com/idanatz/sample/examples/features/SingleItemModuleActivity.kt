package com.idanatz.sample.examples.features

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class SingleItemModuleActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter(recyclerView) {
            itemModules += MessageItem()
        }

        oneAdapter.setItems(modelGenerator.generateMessages(10))
    }

    private class MessageItem : ItemModule<MessageModel>() {
        init {
            config {
                layoutResource = R.layout.message_model
            }
            onBind { model, viewBinder, _ ->
                val title = viewBinder.findViewById<TextView>(R.id.title)
                val body = viewBinder.findViewById<TextView>(R.id.body)
                val image = viewBinder.findViewById<ImageView>(R.id.avatarImage)

                title.text = model.title
                body.text = model.body
                Glide.with(viewBinder.rootView).load(model.avatarImageId).into(image)
            }
        }
    }
}