package com.idanatz.sample.examples.features

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
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
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.models.HeaderModel

class FirstBindAnimationActivity : BaseExampleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(messageItem())
                .attachItemModule(headerItem())

        val items = modelGenerator.addHeadersFromMessages(
                messages = modelGenerator.generateFirstMessages() + modelGenerator.generateLoadMoreMessages(),
                checkable = false
        )
        oneAdapter.setItems(items)
    }

    private fun messageItem(): ItemModule<MessageModel> = object : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.message_model

            override fun withFirstBindAnimation(): Animator {
                // can be implemented by inflating Animator Xml
                return AnimatorInflater.loadAnimator(this@FirstBindAnimationActivity, R.animator.item_animation_example)
            }
        }

        override fun onBind(model: MessageModel, viewBinder: ViewBinder) {
            val title = viewBinder.findViewById<TextView>(R.id.title)
            val body = viewBinder.findViewById<TextView>(R.id.body)
            val image = viewBinder.findViewById<ImageView>(R.id.avatarImage)

            title.text = model.title
            body.text = model.body
            Glide.with(this@FirstBindAnimationActivity).load(model.avatarImageId).into(image)
        }
    }

    private fun headerItem(): ItemModule<HeaderModel> = object : ItemModule<HeaderModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource() = R.layout.header_model

            override fun withFirstBindAnimation(): Animator {
                // can be implemented by constructing ObjectAnimator
                return ObjectAnimator().apply {
                    propertyName = "translationX"
                    setFloatValues(-1080f, 0f)
                    duration = 750
                }
            }
        }

        override fun onBind(model: HeaderModel, viewBinder: ViewBinder) {
            viewBinder.findViewById<TextView>(R.id.header_title).text = model.name
        }
    }
}