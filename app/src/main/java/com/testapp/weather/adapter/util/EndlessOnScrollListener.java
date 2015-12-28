package com.testapp.weather.adapter.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Use this class to provide "load on demand" functionality for RecyclerView
 * Created by romka on 25.12.15.
 */
public class EndlessOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = "EndlessOnScrollListener";
    private final OnLoadMoreListener mMoreListener;

    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    public EndlessOnScrollListener(@NonNull OnLoadMoreListener _listener) {
        mMoreListener = _listener;
    }

    public void setRecyclerView(final RecyclerView _recyclerView) {
        mRecyclerView = _recyclerView;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (recyclerView == null) recyclerView = mRecyclerView;
        super.onScrolled(recyclerView, dx, dy);

        if (dy < 0) return;

        getLayoutManager(recyclerView);
        if (mLayoutManager == null) return;

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLayoutManager.getItemCount();
        firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

        if (!mMoreListener.isLoading()
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

            // End has been reached
            mMoreListener.onLoadMore();

        }
    }

    private void getLayoutManager(RecyclerView _recyclerView) {
        if (mLayoutManager == null) {
            final RecyclerView.LayoutManager lm = _recyclerView.getLayoutManager();
            if (lm instanceof LinearLayoutManager) mLayoutManager = (LinearLayoutManager) lm;
        }
    }
}