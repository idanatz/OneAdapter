package com.idanatz.sample.examples.features

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.airbnb.lottie.LottieAnimationView
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.examples.ActionsDialog.*

class EmptinessModuleActivity : BaseExampleActivity(), ActionsListener {

    private lateinit var oneAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneAdapter = OneAdapter(recyclerView) {
	        itemModules += MessageItem()
	        emptinessModule = EmptinessModuleImpl()
        }

        initActionsDialog(Action.SetAll, Action.ClearAll).setListener(this)
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

    private class EmptinessModuleImpl : EmptinessModule() {
	    init {
		    config {
			    layoutResource = R.layout.empty_state
		    }
		    onBind { viewBinder, _ ->
			    val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
			    animation.setAnimation(R.raw.empty_list)
			    animation.playAnimation()
		    }
		    onUnbind { viewBinder, _ ->
			    val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
			    animation.pauseAnimation()
		    }
	    }
    }

    override fun onSetAllClicked() {
        oneAdapter.setItems(modelGenerator.generateMessages(10))
    }

    override fun onClearAllClicked() {
        oneAdapter.clear()
    }
}