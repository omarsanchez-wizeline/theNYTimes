package com.wizeline.omarsanchez.nytimessearch.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.wizeline.omarsanchez.nytimessearch.interfaces.ScrollInterfece;

public class InfiniteScroll extends RecyclerView.OnScrollListener {

    private StaggeredGridLayoutManager layoutManager;
    private ScrollInterfece listener;
    private boolean isLoading = false;
    private int itemsOnPage = 0;
    private int threshold = 0;

    public InfiniteScroll(StaggeredGridLayoutManager layoutManager, int itemsOnPage, int threshold, ScrollInterfece listener) {
        this.layoutManager = layoutManager;
        this.itemsOnPage = itemsOnPage;
        this.threshold = threshold;
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int[] lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null);
        int itemsCount = layoutManager.getItemCount();
        int page = itemsCount / itemsOnPage;
        int lastVisibleItem = 0;

        for (int itemPosition : lastVisibleItemPositions) {
            if (lastVisibleItem < itemPosition) {
                lastVisibleItem = itemPosition;
            }
        }

        if (lastVisibleItem + 1 >= itemsCount - threshold && !isLoading) {
            listener.OnFinal(page);
        }
    }

    public void loadStarted() {
        isLoading = true;
    }

    public void LoadFinished() {
        isLoading = false;
    }
}