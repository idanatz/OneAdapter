package com.android.oneadapter.internal

import android.util.SparseArray
import android.view.View

class ViewFinder(private val rootView: View, private val cachedViews: SparseArray<View> = SparseArray()) {

    @Suppress("UNCHECKED_CAST")
    fun <V : View> findViewById(id: Int): V {
        var view: View? = cachedViews[id]
        if (view == null) {
            view = rootView.findViewById(id)
            cachedViews.put(id, view)
        }
        return view as V
    }

    fun getRootView() = rootView
}