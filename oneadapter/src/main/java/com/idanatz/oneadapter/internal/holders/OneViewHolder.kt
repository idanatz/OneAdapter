package com.idanatz.oneadapter.internal.holders

import android.animation.Animator
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.idanatz.oneadapter.internal.event_hooks.EventHooksMap
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHook
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.internal.selection.OneItemDetail
import com.idanatz.oneadapter.internal.states.StatesMap
import com.idanatz.oneadapter.internal.utils.extensions.inflateLayout

@Suppress("NAME_SHADOWING")
internal abstract class OneViewHolder<M : Diffable> (
        parent: ViewGroup,
        @LayoutRes layoutResourceId: Int,
        val firstBindAnimation: Animator? = null,
        val statesHooksMap: StatesMap<M>? = null, // Not all ViewHolders will have states configured
        val eventsHooksMap: EventHooksMap<M>? = null // Not all ViewHolders will have events configured
) : RecyclerView.ViewHolder(parent.context.inflateLayout(layoutResourceId, parent, false)) {

    lateinit var viewBinder: ViewBinder
    lateinit var metadata: Metadata
    lateinit var model: M

    // internal flags
    var isSwiping = false

    abstract fun onCreated()
    abstract fun onBind(model: M)
    abstract fun onUnbind(model: M)
    abstract fun onClicked(model: M)
    abstract fun onSwipe(canvas: Canvas, xAxisOffset: Float)
    abstract fun onSwipeComplete(model: M, swipeDirection: SwipeEventHook.SwipeDirection)
    abstract fun onSelected(model: M, selected: Boolean)

    fun onCreateViewHolder() {
        this.viewBinder = ViewBinder(itemView)
        onCreated()
    }

    fun onBindViewHolder(model: M, metadata: Metadata) {
        this.model = model
        this.metadata = metadata

        handleAnimations(metadata.isAnimating)
        handleEventHooks()
        onBind(model)
    }

    fun onBindSelection(model: M, selected: Boolean) {
        if (statesHooksMap?.isSelectionStateConfigured() == true) {
            onSelected(model, selected)
        }
    }

    fun createItemLookupInformation(): OneItemDetail? {
        if (isSwiping) // while swiping disable the option to select
            return null

        return statesHooksMap?.getSelectionState()?.let {
            if (it.isSelectionEnabled(model)) OneItemDetail(adapterPosition, itemId)
            else null
        }
    }

    fun onSwipeViewHolder(canvas: Canvas, xAxisOffset: Float) {
        onSwipe(canvas, xAxisOffset)
    }

    fun onSwipeViewHolderComplete(swipeDirection: SwipeEventHook.SwipeDirection) {
        if (eventsHooksMap?.isSwipeEventHookConfigured() == true) {
            onSwipeComplete(model, swipeDirection)
        }
    }

    private fun handleAnimations(shouldAnimate: Boolean) {
        if (shouldAnimate) {
            firstBindAnimation?.setTarget(itemView)
            firstBindAnimation?.start()
        } else {
            firstBindAnimation?.end()
        }
    }

    private fun handleEventHooks() {
        if (eventsHooksMap?.isClickEventHookConfigured() == true) {
            itemView.setOnClickListener { onClicked(model) }
        }
    }
}