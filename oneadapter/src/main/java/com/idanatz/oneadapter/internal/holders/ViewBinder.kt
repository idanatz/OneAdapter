package com.idanatz.oneadapter.internal.holders

import android.util.SparseArray
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding

/**
 * This class responsibility is to supply findViewById abilities while caching the found views.
 */
class ViewBinder(val rootView: View) {

	private val cachedViews: SparseArray<View> = SparseArray()
	private var viewBinding: ViewBinding? = null

	val dataBinding: ViewDataBinding? by lazy {
        try {
            DataBindingUtil.bind<ViewDataBinding>(rootView)
        } catch (ex: IllegalArgumentException) { // View is not a binding layout
            null
        }
    }

	fun <T> bindings(viewBinding: (View) -> T): T {
		return if (this.viewBinding != null) this.viewBinding as T
		else viewBinding(rootView).also { this.viewBinding = it as ViewBinding }
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