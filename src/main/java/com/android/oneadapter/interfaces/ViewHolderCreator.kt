package com.android.oneadapter.interfaces

import android.view.ViewGroup
import com.android.oneadapter.internal.OneViewHolder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
internal interface ViewHolderCreator<T> {
    fun create(parent: ViewGroup): OneViewHolder<T>
}