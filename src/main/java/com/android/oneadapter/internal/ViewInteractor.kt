package com.android.oneadapter.internal

import android.support.annotation.IdRes
import android.view.View

/**
 * Created by Idan Atsmon on 22/11/2018.
 */
class ViewInteractor internal constructor(private val viewHolder: OneViewHolder<*>) {

    fun <T : View> get(@IdRes id: Int, actionBlock: (T) -> Unit) = actionBlock.invoke(findViewById(id))

    private fun <T : View> findViewById(id: Int): T = viewHolder.findViewById(id) as T
}