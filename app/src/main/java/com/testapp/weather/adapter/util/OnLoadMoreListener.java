package com.testapp.weather.adapter.util;

/**
 * Used with {@link EndlessOnScrollListener} to provide "load on demand"
 * Created by romka on 25.12.15.
 */
public interface OnLoadMoreListener {

    void onLoadMore();
    boolean isLoading();
}
