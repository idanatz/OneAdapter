package com.android.oneadapter.interfaces

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
interface LoadMoreInjector : HolderInjector<Any> {
    fun visibleThreshold(): Int
    fun onLoadMore(currentPage: Int)
}