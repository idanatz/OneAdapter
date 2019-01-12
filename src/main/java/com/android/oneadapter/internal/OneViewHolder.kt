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

    private val viewMap: SparseArray<View> = SparseArray()
    private var viewInteractor: ViewInteractor? = null

    protected abstract fun onBind(data: M, viewInteractor: ViewInteractor)

    constructor(parent: ViewGroup, itemLayoutRes: Int) : this(LayoutInflater.from(parent.context).inflate(itemLayoutRes, parent, false))

    fun bind(data: M?) {
        if (viewInteractor == null) {
            viewInteractor = ViewInteractor(this)
        }

        let2(data, viewInteractor) { nonNullData, injector ->
            onBind(nonNullData, injector)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : View> findViewById(id: Int): V {
        var view: View? = viewMap[id]
        if (view == null) {
            view = itemView.findViewById(id)
            viewMap.put(id, view)
        }
        return view as V
    }
}