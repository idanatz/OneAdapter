package com.idanatz.oneadapter.internal.paging

interface LoadMoreObserver {
	fun shouldHandleLoadingEvent(): Boolean
    fun onLoadingStateChanged(loading: Boolean)
    fun onLoadMore(currentPage: Int)
}