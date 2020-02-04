package com.idanatz.oneadapter.internal.holders

import android.animation.Animator
import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.idanatz.oneadapter.internal.event_hooks.EventHooksMap
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHook
import com.idanatz.oneadapter.internal.selection.OneItemDetail
import com.idanatz.oneadapter.internal.states.StatesMap
import com.idanatz.oneadapter.internal.utils.extensions.inflateLayout
import com.idanatz.oneadapter.internal.utils.extensions.let2

@Suppress("NAME_SHADOWING")
internal abstract class OneViewHolder<M> (
        parent: ViewGroup,
        @LayoutRes layoutResourceId: Int,
        val firstBindAnimation: Animator? = null,
        private val statesHooksMap: StatesMap<M>? = null, // Not all ViewHolders will have states configured
        private val eventsHooksMap: EventHooksMap<M>? = null // Not all ViewHolders will have events configured
) : RecyclerView.ViewHolder(parent.context.inflateLayout(layoutResourceId, parent, false)) {

    lateinit var viewBinder: ViewBinder
    lateinit var metadata: Metadata
    var model: M? = null

    // internal flags
    var isSwiping = false

    abstract fun onCreated()
    abstract fun onBind(model: M?)
    abstract fun onUnbind(model: M?)

    fun onCreateViewHolder() {
        this.viewBinder = ViewBinder(itemView)
        this.metadata = Metadata()
        onCreated()
    }

    fun onBindViewHolder(model: M?, metadata: Metadata) {
        this.model = model
        this.metadata = metadata

        handleAnimations(metadata.isAnimating)
        handleEventHooks()
        onBind(model)
    }

    fun onBindSelection(model: M?, selected: Boolean) {
        let2(statesHooksMap?.getSelectionState(), model) { selectionState, model ->
            selectionState.onSelected(model, selected)
        }
    }

    fun createItemLookupInformation(): OneItemDetail? {
        if (isSwiping) // while swiping disable the option to select
            return null

        return let2(statesHooksMap?.getSelectionState(), model) { selectionState, model ->
            return if (selectionState.isSelectionEnabled(model)) {
                OneItemDetail(adapterPosition, itemId)
            } else
                null
        }
    }

    fun getSwipeEventHook() = eventsHooksMap?.getSwipeEventHook()

    fun onSwipe(canvas: Canvas, xAxisOffset: Float) {
        eventsHooksMap?.getSwipeEventHook()?.onSwipe(canvas, xAxisOffset, viewBinder)
    }

    fun onSwipeComplete(swipeDirection: SwipeEventHook.SwipeDirection) {
        let2(eventsHooksMap?.getSwipeEventHook(), model) { swipeEventHook, model ->
            swipeEventHook.onSwipeComplete(model, swipeDirection, viewBinder)
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
        let2(eventsHooksMap?.getClickEventHook(), model) { clickEventHook, model ->
            itemView.setOnClickListener { clickEventHook.onClick(model, viewBinder) }
        }
    }
}