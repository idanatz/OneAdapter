package com.android.oneadapter.internal.holders

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.oneadapter.modules.holder.HolderModuleConfig
import com.android.oneadapter.internal.selection.OneItemDetail

/**
 * Created by Idan Atsmon on 19/11/2018.
 */

internal abstract class OneViewHolder<in M> (
        parent: ViewGroup,
        private val holderModuleConfig: HolderModuleConfig<M>
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(holderModuleConfig.layoutResource, parent, false)) {

    lateinit var viewBinder: ViewBinder

    abstract fun onBind(model: M?)
    abstract fun onUnbind()
    abstract fun onSelected(model: M?)
    abstract fun onUnSelected(model: M?)

    fun onBindViewHolder(model: M?) {
        this.viewBinder = ViewBinder(itemView)
        onBind(model)
    }

    fun onBindSelection(model: M?, selected: Boolean) = if (selected) onSelected(model) else onUnSelected(model)

    fun createItemLookupInformation() =
            if (holderModuleConfig.selectionEnabled) OneItemDetail(adapterPosition, itemId)
            else null
}