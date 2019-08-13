package com.idanatz.sample.examples.features

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.events.ClickEventHook
import com.idanatz.oneadapter.external.events.SwipeEventHook
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.models.ModelGenerator
import com.idanatz.sample.examples.BaseExampleActivity

class SwipeEventHookActivity : BaseExampleActivity() {

    private lateinit var oneAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneAdapter = OneAdapter()
                .attachItemModule(messageItem().addEventHook(swipeEventHook()))
                .attachTo(recyclerView)

        oneAdapter.setItems(modelGenerator.generateFirstModels())
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
            Glide.with(this@SwipeEventHookActivity).load(model.avatarImageId).into(image)
        }
    }

    private fun swipeEventHook(): SwipeEventHook<MessageModel> {
        return object : SwipeEventHook<MessageModel>(SwipeDirection.Left) {

            override fun onSwipe(canvas: Canvas, xAxisOffset: Float, viewBinder: ViewBinder) {
                var deleteIcon: Drawable?
                var redColorDrawable: ColorDrawable

                when  {
                    xAxisOffset < 0 -> {
                        deleteIcon = ContextCompat.getDrawable(this@SwipeEventHookActivity, R.drawable.ic_delete_white_24dp)
                        redColorDrawable = ColorDrawable(Color.RED)

                        deleteIcon?.let {
                            val rootView = viewBinder.getRootView()
                            val margin = 50
                            val middle = rootView.bottom - rootView.top
                            var top = rootView.top
                            var bottom = rootView.bottom
                            var right = rootView.right
                            var left = rootView.right + xAxisOffset.toInt()
                            redColorDrawable.setBounds(left, top, right, bottom)
                            redColorDrawable.draw(canvas)

                            top = rootView.top + middle / 2 - deleteIcon.intrinsicHeight / 2
                            bottom = top + deleteIcon.intrinsicHeight
                            right = rootView.right - margin
                            left = right - deleteIcon.intrinsicWidth
                            deleteIcon.setBounds(left, top, right, bottom)
                            deleteIcon.draw(canvas)
                        }
                    }
                    xAxisOffset > 0 -> {

                    }
                }
            }

            override fun onSwipeComplete(model: MessageModel, direction: SwipeDirection, viewBinder: ViewBinder) {
                if (direction === SwipeDirection.Left) {
                    oneAdapter.remove(model)
                }
            }
        }
    }
}