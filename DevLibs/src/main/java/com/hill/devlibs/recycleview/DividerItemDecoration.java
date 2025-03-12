package com.hill.devlibs.recycleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;


import com.hill.devlibs.R;
import com.hill.devlibs.tools.Log;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Hill on 2017/11/20.
 * update on 2019/11/22
 * reference:https://juejin.im/post/59099fe844d904006942a983
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;
    private int orientation=RecyclerView.VERTICAL;

    public enum DividerColor{
        White(R.drawable.divider_white),
        Gray(R.drawable.divider_gray),
        LightGray(R.drawable.divider_light_gray),
        ColorLess(R.drawable.divider_colorless)
        ;

        public int Res;
        DividerColor(int r) {
            Res=r;
        }
    }
    public DividerItemDecoration(Context context) {
        this(context, DividerColor.Gray);
    }

    public DividerItemDecoration(Context context, DividerColor color) {
        this(context,color.Res);
    }
    public DividerItemDecoration(Context context, @DrawableRes int dividerRes){
        this(context,dividerRes,RecyclerView.VERTICAL);
    }
    public DividerItemDecoration(Context context, @DrawableRes int dividerRes, int ori){
        mDivider = context.getResources().getDrawable(dividerRes);
        orientation=ori;
    }


    /**
     * 设置条目周边的偏移量
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (orientation == RecyclerView.HORIZONTAL) {
            //画垂直线
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        } else if (orientation == RecyclerView.VERTICAL) {
            //画水平线
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }




    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        int left = parent.getPaddingLeft();
//        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

//            Log.i("onDrawOver child.getBottom()->"+child.getBottom()+" params.bottomMargin->"+params.bottomMargin);

//            int top = child.getBottom() + params.bottomMargin;
//            int bottom = top + mDivider.getIntrinsicHeight();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + params.rightMargin;

            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;


//            Log.w("onDrawOver left->"+left+" top->"+top+" right->"+right+" bottom->"+bottom);

            if (orientation==RecyclerView.VERTICAL){
                mDivider.setBounds(0, top, 0, bottom);
            }else {
                mDivider.setBounds(left, 0, right, 0);
            }

            mDivider.draw(c);
//            child.offsetTopAndBottom(mDivider.getIntrinsicHeight());
        }
    }

    /**
     * 画线
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (orientation == RecyclerView.HORIZONTAL) {
            drawVertical(c, parent, state);
        } else if (orientation == RecyclerView.VERTICAL) {
            drawHorizontal(c, parent, state);
        }
    }

    /**
     * 画竖直分割线
     */
    private void drawVertical(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int top = child.getTop() - params.topMargin;
            int right = left + mDivider.getIntrinsicWidth();
            int bottom = child.getBottom() + params.bottomMargin;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 画水平分割线
     */
    private void drawHorizontal(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int top = child.getBottom() + params.bottomMargin;
            int right = child.getRight() + params.rightMargin;
            int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}
