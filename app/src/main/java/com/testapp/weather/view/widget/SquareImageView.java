package com.testapp.weather.view.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by d_rom on 25.12.2015.
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        final int size;
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode != MeasureSpec.AT_MOST){
            size = getMeasuredHeight();
        } else if (widthSpecMode != MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            size = getMeasuredWidth();
        } else {
            size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        }
        setMeasuredDimension(size, size);
    }
}
