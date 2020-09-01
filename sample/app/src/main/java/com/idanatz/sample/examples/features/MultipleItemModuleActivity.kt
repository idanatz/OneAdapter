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
import com.idanatz.sample.models.HeaderModel

class MultipleItemModuleActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter(recyclerView) {
            itemModules += MessageItem()
            itemModules += HeaderItem()
        }

        val items = modelGenerator.addHeadersFromMessages(
                messages = modelGenerator.generateMessages(10),
                checkable = false
        )
        oneAdapter.setItems(items)
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

    private class HeaderItem : ItemModule<HeaderModel>() {
        init {
            config {
                layoutResource = R.layout.header_model
            }
            onBind { model, viewBinder, _ ->
                viewBinder.findViewById<TextView>(R.id.header_title).text = model.name
            }
        }
    }
}