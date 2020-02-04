package com.idanatz.oneadapter.internal.holders

import android.util.SparseArray
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * This class responsibility is to supply findViewById abilities while caching the found views.
 */
class ViewBinder(
        val rootView: View,
        private val cachedViews: SparseArray<View> = SparseArray()
) {

    val dataBinding: ViewDataBinding? by lazy {
        try {
            DataBindingUtil.bind<ViewDataBinding>(rootView)
        } catch (ex: IllegalArgumentException) { // View is not a binding layout
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : View> findViewById(id: Int): V {
        var view: View? = cachedViews[id]
        if (view == null) {
            view = rootView.findViewById(id)
            cachedViews.put(id, view)
        }
        return view as V
    }
}