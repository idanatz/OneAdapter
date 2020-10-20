package com.idanatz.oneadapter.internal.selection

import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModuleConfig
import com.idanatz.oneadapter.external.states.SelectionStateConfig
import com.idanatz.oneadapter.internal.utils.extensions.toOneViewHolder
import java.util.*

@Suppress("UNCHECKED_CAST")
internal class OneSelectionHandler(
		selectionModule: ItemSelectionModule,
		val recyclerView: RecyclerView
) : SelectionTracker.SelectionObserver<Long>() {

	private val ghostKey = UUID.randomUUID().mostSignificantBits
	private var previousSelectionCount: Int = 0

	private val itemKeyProvider: OneItemKeyProvider = OneItemKeyProvider(recyclerView)
	private val selectionTracker: SelectionTracker<Long> = SelectionTracker.Builder(
			recyclerView.id.toString(),
			recyclerView,
			itemKeyProvider,
			OneItemDetailLookup(recyclerView),
			StorageStrategy.createLongStorage()
	)
	.withSelectionPredicate(object : SelectionTracker.SelectionPredicate<Long>() {
		override fun canSetStateForKey(key: Long, nextState: Boolean): Boolean {
			if (key == ghostKey)
				return true // always accept let the ghost key

			val forbidSelection = recyclerView.findViewHolderForItemId(key)?.toOneViewHolder()?.let { holder ->
				holder.statesHooksMap?.getSelectionState()?.config?.let { selectionStateConfig ->
					selectionStateConfig.selectionTrigger == SelectionStateConfig.SelectionTrigger.Manual && !isInManualSelection()
				} ?: true
			} ?: true

			return !forbidSelection
		}

		override fun canSetStateAtPosition(position: Int, nextState: Boolean): Boolean = true

		override fun canSelectMultiple(): Boolean = when (selectionModule.config.selectionType) {
			ItemSelectionModuleConfig.SelectionType.Single -> false
			ItemSelectionModuleConfig.SelectionType.Multiple -> true
		}
	})
	.build()
	.also { it.addObserver(this) }

	var observer: SelectionObserver? = null

	fun startSelection() {
		selectionTracker.select(ghostKey)
	}

	fun clearSelection(): Boolean = selectionTracker.clearSelection()

	fun getSelectedPositions(): List<Int> {
		return selectionTracker.selection?.map { key -> itemKeyProvider.getPosition(key) }?.filter { it >= 0 } ?: emptyList()
	}

	fun inSelectionActive(): Boolean {
		return selectionTracker.selection.size() != 0
	}

	fun isPositionSelected(position: Int): Boolean {
		return selectionTracker.isSelected(itemKeyProvider.getKey(position))
	}

	override fun onItemStateChanged(key: Long, selected: Boolean) {
		recyclerView.findViewHolderForItemId(key)?.toOneViewHolder()?.let { holder ->
			observer?.onItemStateChanged(holder, itemKeyProvider.getPosition(key), selected)
		}
	}

	override fun onSelectionChanged() {
		val currentSelectionCount = selectionTracker.selection.size()

		when {
			previousSelectionCount == 0 && currentSelectionCount > 0 -> observer?.onSelectionStarted()
			previousSelectionCount > 0 && currentSelectionCount == 0 -> observer?.onSelectionEnded()
		}

		// ghost key is not an actual item but a trick to start manual selection
		// no need to include it in the selection count
		val countToNotify =
				if (isInManualSelection()) currentSelectionCount - 1
				else currentSelectionCount
		observer?.onSelectionUpdated(countToNotify)

		previousSelectionCount = currentSelectionCount
	}

	private fun isInManualSelection() = selectionTracker.isSelected(ghostKey)
}