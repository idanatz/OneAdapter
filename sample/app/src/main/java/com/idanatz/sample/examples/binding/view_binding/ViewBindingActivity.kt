package com.idanatz.sample.examples.binding.view_binding

import android.os.Bundle

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.sample.R
import com.idanatz.oneadapter.sample.databinding.MessageModelBinding
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class ViewBindingActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter(recyclerView) {
            itemModules += MessageItemModule()
        }

        oneAdapter.setItems(modelGenerator.generateMessages(10))
    }

    class MessageItemModule : ItemModule<MessageModel>() {

        init {
            config {
                layoutResource = R.layout.message_model
            }
            onBind { model, viewBinder, _ ->
				viewBinder.bindings(MessageModelBinding::bind).run {
					title.text = model.title
					body.text = model.body
					Glide.with(viewBinder.rootView).load(model.avatarImageId).into(avatarImage)
				}
            }
        }
    }
}