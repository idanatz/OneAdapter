package com.android.oneadapter.internal.interfaces

import android.view.ViewGroup
import com.android.oneadapter.internal.holders.OneViewHolder

internal interface ViewHolderCreator<M> {
    fun create(parent: ViewGroup): OneViewHolder<M>
}