package com.idanatz.oneadapter.internal.selection

import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModuleConfig
import com.idanatz.oneadapter.internal.holders.OneViewHolder
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
	.withSelectionPredicate(when (selectionModule.provideModuleConfig().withSelectionType()) {
		ItemSelectionModuleConfig.SelectionType.Single -> SelectionPredicates.createSelectSingleAnything()
		ItemSelectionModuleConfig.SelectionType.Multiple -> SelectionPredicates.createSelectAnything()
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
		recyclerView.findViewHolderForItemId(key)?.let { holder ->
			observer?.onItemStateChanged(holder as OneViewHolder<Diffable>, itemKeyProvider.getPosition(key), selected)
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
				if (selectionTracker.isSelected(ghostKey)) currentSelectionCount - 1
				else currentSelectionCount
		observer?.onSelectionUpdated(countToNotify)

		previousSelectionCount = currentSelectionCount
	}
}