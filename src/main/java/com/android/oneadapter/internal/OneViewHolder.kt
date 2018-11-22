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
internal abstract class OneViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val viewMap: SparseArray<View> = SparseArray()
    private var viewInteractor: ViewInteractor? = null

    protected abstract fun onBind(data: T, viewInteractor: ViewInteractor)

    constructor(parent: ViewGroup, itemLayoutRes: Int) : this(LayoutInflater.from(parent.context).inflate(itemLayoutRes, parent, false)) {}

    fun bind(data: T?) {
        if (viewInteractor == null) {
            viewInteractor = ViewInteractor(this)
        }

        let2(data, viewInteractor) { data, injector ->
            onBind(data, injector)
        }
    }

    fun <T : View> findViewById(id: Int): T {
        var view: View? = viewMap.get(id)
        if (view == null) {
            view = itemView.findViewById(id)
            viewMap.put(id, view)
        }
        return view as T
    }
}