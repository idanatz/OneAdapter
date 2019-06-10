package com.android.oneadapter.internal.holders

import android.util.SparseArray
import android.view.View

/**
 * This class responsibility is to supply findViewById abilities while caching the found views.
 */
class ViewBinder(private val rootView: View, private val cachedViews: SparseArray<View> = SparseArray()) {

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