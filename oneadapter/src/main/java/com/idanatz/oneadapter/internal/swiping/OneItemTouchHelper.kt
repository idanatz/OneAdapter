package com.idanatz.oneadapter.internal.swiping

import android.graphics.Canvas
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHook
import com.idanatz.oneadapter.internal.utils.extensions.toOneViewHolder

private const val DISABLE_VALUE = 0
private const val SWIPING_STATE_EXTRA_DELAY_MILLIS = 250L

internal class OneItemTouchHelper : ItemTouchHelper(OneItemTouchHelperCallback()) {

    internal class OneItemTouchHelperCallback : ItemTouchHelper.SimpleCallback(DISABLE_VALUE, DISABLE_VALUE) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false

        override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            return viewHolder.toOneViewHolder().eventsHooksMap?.getSwipeEventHook()?.let { swipeEventHook ->
                swipeEventHook.provideHookConfig().withSwipeDirection().map { mapDirection(it) }.reduce { accumulator, current -> accumulator or current }
            } ?: super.getSwipeDirs(recyclerView, viewHolder)
        }

        /**
         * Called when the view holder is completely swiped from the screen
         */
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            mapDirection(direction, ViewCompat.getLayoutDirection(viewHolder.itemView))?.let { viewHolder.toOneViewHolder().onSwipeViewHolderComplete(it) }
        }

        /**
         * Called while the view holder is swiping
         */
        override fun onChildDraw(canvas: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            viewHolder.toOneViewHolder().onSwipeViewHolder(canvas, dX)
            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        /**
         * Called after the view holder interaction is over and it also completed its animation.
         */
        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.handler.postDelayed({ viewHolder.toOneViewHolder().isSwiping = false }, SWIPING_STATE_EXTRA_DELAY_MILLIS)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState == ACTION_STATE_SWIPE) viewHolder?.toOneViewHolder()?.isSwiping = true
        }

        private fun mapDirection(itemTouchHelperDirection: Int, layoutDirection: Int): SwipeEventHook.SwipeDirection? = when (convertToRelativeDirection(itemTouchHelperDirection, layoutDirection)) {
            START -> SwipeEventHook.SwipeDirection.Start
            END -> SwipeEventHook.SwipeDirection.End
            UP -> SwipeEventHook.SwipeDirection.Up
            DOWN -> SwipeEventHook.SwipeDirection.Down
            else -> null
        }

        private fun mapDirection(swipeDirection: SwipeEventHook.SwipeDirection): Int = when (swipeDirection) {
            SwipeEventHook.SwipeDirection.Start -> START
            SwipeEventHook.SwipeDirection.End -> END
            SwipeEventHook.SwipeDirection.Up -> UP
            SwipeEventHook.SwipeDirection.Down -> DOWN
        }
    }
}