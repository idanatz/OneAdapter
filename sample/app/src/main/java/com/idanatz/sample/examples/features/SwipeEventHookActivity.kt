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
import com.idanatz.oneadapter.external.modules.ItemModule
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

		oneAdapter = OneAdapter(recyclerView) {
			itemModules += MessageItem()
		}

		oneAdapter.setItems(modelGenerator.generateMessages(10))
	}

	private inner class MessageItem : ItemModule<MessageModel>() {
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
			eventHooks += SwipeEventHook<MessageModel>().apply {
				config {
					swipeDirection = listOf(SwipeEventHook.SwipeDirection.Start, SwipeEventHook.SwipeDirection.End)
				}
				onSwipe { canvas, xAxisOffset, viewBinder ->
					when {
						xAxisOffset < 0 -> paintSwipeLeft(canvas, xAxisOffset, viewBinder.rootView)
						xAxisOffset > 0 -> paintSwipeRight(canvas, xAxisOffset, viewBinder.rootView)
					}
				}
				onSwipeComplete { model, _, metadata ->
					when (metadata.swipeDirection) {
						SwipeEventHook.SwipeDirection.Start -> oneAdapter.remove(model)
						SwipeEventHook.SwipeDirection.End -> {
							Toast.makeText(this@SwipeEventHookActivity, "${model.title} snoozed", Toast.LENGTH_SHORT).show()
							oneAdapter.update(metadata.position) // for resetting the view back into place
						}
					}
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