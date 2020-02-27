package com.idanatz.oneadapter.internal.holders_creators

import android.view.ViewGroup
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.internal.holders.OneViewHolder

internal interface ViewHolderCreator<M : Diffable> {
    fun create(parent: ViewGroup): OneViewHolder<M>
}