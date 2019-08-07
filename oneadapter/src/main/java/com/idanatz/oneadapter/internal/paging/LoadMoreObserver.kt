package com.idanatz.oneadapter.internal.paging

interface LoadMoreObserver {
    fun onLoadingStateChanged(loading: Boolean)
    fun onLoadMore(currentPage: Int)
}