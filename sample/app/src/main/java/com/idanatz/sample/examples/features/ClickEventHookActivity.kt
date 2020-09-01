package com.idanatz.sample.examples.features

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class ClickEventHookActivity : BaseExampleActivity() {

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
            eventHooks += ClickEventHook<MessageModel>().apply {
                onClick { model, viewBinder, _ ->
                    Toast.makeText(viewBinder.rootView.context, "${model.title} clicked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}