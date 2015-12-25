package com.testapp.weather.adapter.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.testapp.weather.R;

/**
 * Created on 25.12.15.
 */
public class ItemDivider extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;

    private final int mLayoutOrientation;
    private int mSizeGridSpacingPx;
    private int mDividerFromPosition;

    public ItemDivider(Context _context, final int _dividerFromPosition) {
        this(_context, _dividerFromPosition, HORIZONTAL);
    }

    public ItemDivider(Context _context, final int _dividerFromPosition, int _orientation) {
        mSizeGridSpacingPx = _context.getResources().getDimensionPixelSize(R.dimen.divider_size_list);
        mDividerFromPosition = _dividerFromPosition;
        mLayoutOrientation = _orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (itemPosition < 0) return;
        if (itemPosition <= mDividerFromPosition) {
            if ((mLayoutOrientation & VERTICAL) == VERTICAL) outRect.top = 0;
            if ((mLayoutOrientation & HORIZONTAL) == HORIZONTAL) outRect.left = 0;
        } else {
            if ((mLayoutOrientation & VERTICAL) == VERTICAL) outRect.top = mSizeGridSpacingPx;
            if ((mLayoutOrientation & HORIZONTAL) == HORIZONTAL) outRect.left = mSizeGridSpacingPx;
        }
        if ((mLayoutOrientation & VERTICAL) == VERTICAL) outRect.bottom = 0;
        if ((mLayoutOrientation & HORIZONTAL) == HORIZONTAL) outRect.right = 0;
    }
}
