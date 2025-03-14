package com.hill.viewpagerlib.recycleview;

import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hill.viewpagerlib.R;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An implement of {@link RecyclerView} which support auto play.
 */

public class AutoPlayRecyclerView extends RecyclerView {
    private AutoPlaySnapHelper autoPlaySnapHelper;

    public AutoPlayRecyclerView(Context context) {
        this(context, null);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoPlayRecyclerView);
        final int timeInterval = typedArray.getInt(R.styleable.AutoPlayRecyclerView_timeInterval, AutoPlaySnapHelper.TIME_INTERVAL);
        final int direction = typedArray.getInt(R.styleable.AutoPlayRecyclerView_slide_direction, AutoPlaySnapHelper.RIGHT);
        typedArray.recycle();
        autoPlaySnapHelper = new AutoPlaySnapHelper(timeInterval, direction);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (autoPlaySnapHelper != null) {
                    autoPlaySnapHelper.pause();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (autoPlaySnapHelper != null) {
                    autoPlaySnapHelper.start();
                }
        }
        return result;
    }

    public void start() {
        autoPlaySnapHelper.start();
    }

    public void pause() {
        autoPlaySnapHelper.pause();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        autoPlaySnapHelper.attachToRecyclerView(this);
    }
}
