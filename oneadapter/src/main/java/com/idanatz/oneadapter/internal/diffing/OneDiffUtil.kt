package com.idanatz.oneadapter.internal.diffing

import androidx.recyclerview.widget.DiffUtil
import com.idanatz.oneadapter.external.interfaces.Diffable

internal class OneDiffUtil : DiffUtil.ItemCallback<Diffable>() {

	override fun areItemsTheSame(oldItem: Diffable, newItem: Diffable) = oldItem.javaClass.name.hashCode() == newItem.javaClass.name.hashCode() && oldItem.uniqueIdentifier == newItem.uniqueIdentifier
	override fun areContentsTheSame(oldItem: Diffable, newItem: Diffable) = oldItem.javaClass.name.hashCode() == newItem.javaClass.name.hashCode() && oldItem.areContentTheSame(newItem)
}