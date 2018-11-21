package com.android.oneadapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.android.oneadapter.interfaces.LoadMoreInjector;

/**
 * Created by Idan Atsmon on 19/11/2018.
 */
public class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    private RecyclerView.LayoutManager layoutManager;
    private LoadMoreInjector loadMoreInjector;
    private boolean includeEmptyState;

    public EndlessRecyclerViewScrollListener(RecyclerView.LayoutManager layoutManager, LoadMoreInjector loadMoreInjector, boolean includeEmptyState) {
        if (layoutManager instanceof GridLayoutManager) {
            this.layoutManager = layoutManager;
            visibleThreshold = loadMoreInjector.visibleThreshold() * ((GridLayoutManager)layoutManager).getSpanCount();
        } else if (layoutManager instanceof LinearLayoutManager) {
            this.layoutManager = layoutManager;
            this.visibleThreshold = loadMoreInjector.visibleThreshold();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            this.layoutManager = layoutManager;
            visibleThreshold = loadMoreInjector.visibleThreshold() * ((StaggeredGridLayoutManager)layoutManager).getSpanCount();
        }
        this.loadMoreInjector = loadMoreInjector;
        this.includeEmptyState = includeEmptyState;

        resetState();
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = layoutManager.getItemCount();

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            loadMoreInjector.onLoadMore(currentPage);
            loading = true;
        }
    }

    // Call this method whenever performing new searches
    public void resetState() {
        currentPage = this.startingPageIndex;
        loading = true;
        if (includeEmptyState) {
            previousTotalItemCount = 1;
        } else {
            previousTotalItemCount = 0;
        }
    }

    private int getLastVisibleItem(int[] lastVisibleItemPositions) {
        int maxSize = 0;
        for (int i = 0; i < lastVisibleItemPositions.length; i++) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i];
            }
            else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i];
            }
        }
        return maxSize;
    }
}