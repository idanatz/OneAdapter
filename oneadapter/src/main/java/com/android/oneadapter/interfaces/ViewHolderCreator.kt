package com.android.oneadapter.interfaces

import android.view.ViewGroup
import com.android.oneadapter.internal.holders.OneViewHolder

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
internal interface ViewHolderCreator<in M> {
    fun create(parent: ViewGroup): OneViewHolder<M>
}