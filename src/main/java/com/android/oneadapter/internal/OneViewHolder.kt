package com.android.oneadapter.internal

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.oneadapter.utils.let2

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
internal abstract class OneViewHolder<in M>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected abstract fun onBind(data: M, inflatedView: View)

    constructor(parent: ViewGroup, itemLayoutRes: Int) : this(LayoutInflater.from(parent.context).inflate(itemLayoutRes, parent, false))

    fun onBindViewHolder(data: M?) {
        data?.let {
            onBind(it, itemView.rootView)
        }
    }
}