package com.idanatz.sample.examples.features

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHook
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHookConfig
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class SwipeEventHookActivity : BaseExampleActivity() {

    companion object {
        const val ICON_MARGIN = 50
    }

    private lateinit var oneAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(MessageItem().addEventHook(MessageSwipeHook()))

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
            Glide.with(viewBinder.rootView).load(item.model.avatarImageId).into(image)
        }
    }

    private inner class MessageSwipeHook : SwipeEventHook<MessageModel>() {
        override fun provideHookConfig(): SwipeEventHookConfig = object : SwipeEventHookConfig() {
            override fun withSwipeDirection() = listOf(SwipeDirection.Left, SwipeDirection.Right)
        }

        override fun onSwipe(canvas: Canvas, xAxisOffset: Float, viewBinder: ViewBinder) {
            when {
                xAxisOffset < 0 -> paintSwipeLeft(canvas, xAxisOffset, viewBinder.rootView)
                xAxisOffset > 0 -> paintSwipeRight(canvas, xAxisOffset, viewBinder.rootView)
            }
        }

        override fun onSwipeComplete(model: MessageModel, direction: SwipeDirection, viewBinder: ViewBinder) {
            when (direction) {
                SwipeDirection.Left -> oneAdapter.remove(model)
                SwipeDirection.Right -> {
                    Toast.makeText(this@SwipeEventHookActivity, "${model.title} snoozed", Toast.LENGTH_SHORT).show()
                    oneAdapter.update(model) // for resetting the view back into place
                }
            }
        }
    }

    private fun paintSwipeRight(canvas: Canvas, xAxisOffset: Float, rootView: View) {
        val icon = ContextCompat.getDrawable(this@SwipeEventHookActivity, R.drawable.ic_snooze_white_24dp)
        val colorDrawable = ColorDrawable(Color.DKGRAY)

        icon?.let {
            val middle = rootView.bottom - rootView.top
            var top = rootView.top
            var bottom = rootView.bottom
            var right = rootView.left + xAxisOffset.toInt()
            var left = rootView.left
            colorDrawable.setBounds(left, top, right, bottom)
            colorDrawable.draw(canvas)

            top = rootView.top + (middle / 2) - (it.intrinsicHeight / 2)
            bottom = top + it.intrinsicHeight
            left = rootView.left + ICON_MARGIN
            right = left + it.intrinsicWidth
            it.setBounds(left, top, right, bottom)
            it.draw(canvas)
        }
    }

    private fun paintSwipeLeft(canvas: Canvas, xAxisOffset: Float, rootView: View) {
        val icon = ContextCompat.getDrawable(this@SwipeEventHookActivity, R.drawable.ic_delete_white_24dp)
        val colorDrawable = ColorDrawable(Color.RED)

        icon?.let {
            val middle = rootView.bottom - rootView.top
            var top = rootView.top
            var bottom = rootView.bottom
            var right = rootView.right
            var left = rootView.right + xAxisOffset.toInt()
            colorDrawable.setBounds(left, top, right, bottom)
            colorDrawable.draw(canvas)

            top = rootView.top + (middle / 2) - (it.intrinsicHeight / 2)
            bottom = top + it.intrinsicHeight
            right = rootView.right - ICON_MARGIN
            left = right - it.intrinsicWidth
            it.setBounds(left, top, right, bottom)
            it.draw(canvas)
        }
    }
}