package com.idanatz.oneadapter.internal.interfaces

import android.view.ViewGroup
import com.idanatz.oneadapter.internal.holders.OneViewHolder

internal interface ViewHolderCreator<M> {
    fun create(parent: ViewGroup): OneViewHolder<M>
}