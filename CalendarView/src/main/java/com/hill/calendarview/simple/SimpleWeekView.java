package com.hill.calendarview.simple;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;

/**
 * 简单周视图
 * Created by huanghaibin on 2017/11/29.
 */

public class SimpleWeekView extends WeekView {
    private int mRadius;
    private int mPadding;
    /**
     * 圆点半径
     */
    private float mPointRadius;
    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    public SimpleWeekView(Context context) {
        super(context);
        setLayerType(View.LAYER_TYPE_SOFTWARE,mSelectedPaint);

        mPadding = dipToPx(context, 8);
        mPointRadius = dipToPx(context, 4);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);

        //4.0以上硬件加速会导致无效
//        mSelectedPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 10 * 5;
//        mSchemePaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
//        int cx = x + mItemWidth / 2;
//        int cy = mItemHeight / 2;
//        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
        drawSchemePoint(canvas,x,mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine;
        int cx = x + mItemWidth / 2;
        boolean isInRange = isInRange(calendar);
        boolean isEnable = !onCalendarIntercept(calendar);
        if(hasScheme){
            mPointPaint.setColor(calendar.getSchemeColor());
            drawSchemePoint(canvas,x,mPointPaint);
        }
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange && isEnable? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() && isInRange && isEnable? mCurMonthTextPaint : mOtherMonthTextPaint);
//            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
//                    calendar.isCurrentDay() ? mCurDayTextPaint :
//                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mCurMonthTextPaint);
        }
    }
    private void drawSchemePoint(Canvas canvas,int x,Paint paint){
        canvas.drawCircle(x + mItemWidth / 2,mItemHeight - mPadding, mPointRadius,paint);
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
