package com.android.oneadapter.internal

import android.support.annotation.IdRes
import android.view.View
import com.android.oneadapter.interfaces.GenericRunnable

/**
 * Created by Idan Atsmon on 22/11/2018.
 */
class ViewInteractor internal constructor(private val viewHolder: OneViewHolder<*>) {

    fun <V : View> interact(@IdRes id: Int, runnable: GenericRunnable<V>) = runnable.run(findViewById(id))

    private fun <V : View> findViewById(id: Int): V = viewHolder.findViewById(id) as V
}