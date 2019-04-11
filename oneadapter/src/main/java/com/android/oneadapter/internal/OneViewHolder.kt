package com.android.oneadapter.internal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
internal abstract class OneViewHolder<in M> (
        parent: ViewGroup,
        itemLayoutRes: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(itemLayoutRes, parent, false)) {

    lateinit var viewFinder: ViewFinder

    protected abstract fun onBind(model: M?)
    abstract fun onUnbind()

    fun onBindViewHolder(model: M?) {
        viewFinder = ViewFinder(itemView)
        onBind(model)
    }
}