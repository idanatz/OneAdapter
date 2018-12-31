package com.android.oneadapter.internal

import android.support.annotation.IdRes
import android.view.View
import com.android.oneadapter.interfaces.GenericRunnable

/**
 * Created by Idan Atsmon on 22/11/2018.
 */
class ViewInteractor internal constructor(private val viewHolder: OneViewHolder<*>) {

//    fun <T : View> get(@IdRes id: Int, actionBlock: (T) -> Unit) = actionBlock.invoke(findViewById(id))
    fun <T : View> interact(@IdRes id: Int, runnable: GenericRunnable<T>) = runnable.run(findViewById(id))

    private fun <T : View> findViewById(id: Int): T = viewHolder.findViewById(id) as T
}