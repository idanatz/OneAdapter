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
import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.external.modules.EmptinessModuleConfig
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.examples.ActionsDialog.*

class EmptinessModuleActivity : BaseExampleActivity(), ActionsListener {

    private lateinit var oneAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneAdapter = OneAdapter()
                .attachItemModule(messageItem())
                .attachEmptinessModule(emptinessModule())
                .attachTo(recyclerView)

        initActionsDialog(Action.SetAll, Action.ClearAll).setListener(this)
    }

    private fun messageItem(): ItemModule<MessageModel> = object : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.message_model
        }

        override fun onBind(model: MessageModel, viewBinder: ViewBinder) {
            val title = viewBinder.findViewById<TextView>(R.id.title)
            val body = viewBinder.findViewById<TextView>(R.id.body)
            val image = viewBinder.findViewById<ImageView>(R.id.avatarImage)

            title.text = model.title
            body.text = model.body
            Glide.with(this@EmptinessModuleActivity).load(model.avatarImageId).into(image)
        }
    }

    private fun emptinessModule(): EmptinessModule = object : EmptinessModule() {
        override fun provideModuleConfig(): EmptinessModuleConfig = object : EmptinessModuleConfig() {
            override fun withLayoutResource() = R.layout.empty_state
        }

        override fun onBind(viewBinder: ViewBinder) {
            val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
            animation.setAnimation(R.raw.empty_list)
            animation.playAnimation()
        }

        override fun onUnbind(viewBinder: ViewBinder) {
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