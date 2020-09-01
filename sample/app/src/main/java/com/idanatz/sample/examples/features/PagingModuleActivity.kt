package com.idanatz.sample.examples.features

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class PagingModuleActivity : BaseExampleActivity() {

    private lateinit var oneAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneAdapter = OneAdapter(recyclerView) {
	        itemModules += MessageItem()
	        pagingModule = PagingModuleImpl()
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

    private inner class PagingModuleImpl : PagingModule() {
	    init {
		    config {
			    layoutResource = R.layout.load_more
			    visibleThreshold = 3
		    }
			onLoadMore {
				handler.postDelayed({
					oneAdapter.add(modelGenerator.generateMessages(10))
				}, 1000)
			}
	    }
    }
}