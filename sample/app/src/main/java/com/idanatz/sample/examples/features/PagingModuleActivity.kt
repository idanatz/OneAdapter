package com.idanatz.sample.examples.features

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.external.modules.PagingModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class PagingModuleActivity : BaseExampleActivity() {

    private lateinit var oneAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(MessageItem())
                .attachPagingModule(PagingModuleImpl())

        oneAdapter.setItems(modelGenerator.generateFirstMessages())
    }

    private class MessageItem : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.message_model
        }

	    override fun onBind(item: Item<MessageModel>, viewBinder: ViewBinder) {
		    val title = viewBinder.findViewById<TextView>(R.id.title)
		    val body = viewBinder.findViewById<TextView>(R.id.body)
		    val image = viewBinder.findViewById<ImageView>(R.id.avatarImage)

		    title.text = item.model.title
		    body.text = item.model.body
		    Glide.with(viewBinder.rootView).load(item.model.avatarImageId).into(image)	    }
    }

    private inner class PagingModuleImpl : PagingModule() {
        override fun provideModuleConfig(): PagingModuleConfig = object : PagingModuleConfig() {
            override fun withLayoutResource() = R.layout.load_more
            override fun withVisibleThreshold() = 3
        }

        override fun onLoadMore(currentPage: Int) {
            handler.postDelayed({
                oneAdapter.add(modelGenerator.generateLoadMoreMessages())
            }, 1000)
        }
    }
}