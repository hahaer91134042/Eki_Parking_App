package com.eki.parking.View.abs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;

/**
 * Created by Hill on 2017/11/17.
 */

public abstract class UnderLineLayout extends RelativeCustomView {


    protected Paint mPaint;
    private int lineColor= Color.BLACK;

    public UnderLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint=new Paint();
        mPaint.setColor(lineColor);
    }

//    protected abstract RelativeLayout.LayoutParams initNewLayoutParams();
//
//    protected abstract int setInflatView();

    public void setLineColor(@ColorInt int color) {
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // TODO: 2017/11/14 In view group onDraw is not used.
        float viewTotalHight = getMeasuredHeight();
        float viewTotalWidth = getMeasuredWidth();
//        PrintLogKt.d("AccoundInput","H->"+viewTotalHight+" W->"+viewTotalWidth);
//        drawLine(float startX, float startY, float stopX, float stopY,@NonNull Paint paint)
//        canvas.drawLine(0,viewHight,viewWidth,viewHight/2,mPaint);
//        drawRect(float left, float top, float right, float bottom, @NonNull Paint paint)
        canvas.drawRect(0, viewTotalHight - 2f, viewTotalWidth, viewTotalHight, mPaint);
    }
}
