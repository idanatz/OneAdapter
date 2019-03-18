package com.android.oneadapter.internal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
internal abstract class OneViewHolder<in M>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected abstract fun onBind(data: M, inflatedView: View)

    constructor(parent: ViewGroup, itemLayoutRes: Int) : this(LayoutInflater.from(parent.context).inflate(itemLayoutRes, parent, false))

    fun onBindViewHolder(model: M?) {
        model?.let {
            onBind(it, itemView.rootView)
        }
    }
}