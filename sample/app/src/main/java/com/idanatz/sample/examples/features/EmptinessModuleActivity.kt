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
import com.airbnb.lottie.LottieAnimationView
import com.idanatz.oneadapter.external.holders.EmptyIndicator
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.external.modules.EmptinessModuleConfig
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.examples.ActionsDialog.*

class EmptinessModuleActivity : BaseExampleActivity(), ActionsListener {

    private lateinit var oneAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(MessageItem())
                .attachEmptinessModule(EmptinessModuleImpl())

        initActionsDialog(Action.SetAll, Action.ClearAll).setListener(this)
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
		    Glide.with(viewBinder.rootView).load(item.model.avatarImageId).into(image)
	    }
    }

    private class EmptinessModuleImpl : EmptinessModule() {
        override fun provideModuleConfig(): EmptinessModuleConfig = object : EmptinessModuleConfig() {
            override fun withLayoutResource() = R.layout.empty_state
        }

	    override fun onBind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) {
		    val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
		    animation.setAnimation(R.raw.empty_list)
		    animation.playAnimation()
	    }

	    override fun onUnbind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) {
		    val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
		    animation.pauseAnimation()
	    }
    }

    override fun onSetAllClicked() {
        oneAdapter.setItems(modelGenerator.generateFirstMessages())
    }

    override fun onClearAllClicked() {
        oneAdapter.clear()
    }
}