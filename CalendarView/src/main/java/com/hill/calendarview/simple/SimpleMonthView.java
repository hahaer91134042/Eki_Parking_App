package com.hill.calendarview.simple;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

public class SimpleMonthView extends MonthView {

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

    public SimpleMonthView(Context context) {
        super(context);
        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE,mSelectedPaint);

        mPadding = dipToPx(context, 8);
        mPointRadius = dipToPx(context, 4);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);

        //4.0以上硬件加速会导致无效
//        mSelectedPaint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.SOLID));
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 10 * 5;
//        mSchemePaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
//        Log.e("Hill->","onDrawSelected->"+calendar);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
//        int cx = x + mItemWidth / 2;
//        int cy = y + mItemHeight / 2;
//        Log.w("Hill->","SimpleMonthView onDrawScheme->"+calendar);
//        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);

//        boolean isSelected = isSelected(calendar);
//        if (isSelected) {
//            mPointPaint.setColor(Color.WHITE);
//        } else {
//            mPointPaint.setColor(Color.GRAY);
//        }

//        mPointPaint.setColor(mSchemePaint.getColor());
//        Log.d("Hill->","onDrawScheme ->"+calendar);
//        canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - 3 * mPadding, mPointRadius, mSchemePaint);
        drawSchemePoint(canvas,x,y,mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        boolean isInRange = isInRange(calendar);
        boolean isEnable = !onCalendarIntercept(calendar);

//        Log.w("Hill->","onDrawText  hasScheme->"+hasScheme+"  calendar->"+calendar);

        if(hasScheme){
            mPointPaint.setColor(calendar.getSchemeColor());
            drawSchemePoint(canvas,x,y,mPointPaint);
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
//                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    private void drawSchemePoint(Canvas canvas,int x, int y,Paint paint){
        canvas.drawCircle(x + mItemWidth / 2, y + mItemHeight - mPadding, mPointRadius,paint);
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
