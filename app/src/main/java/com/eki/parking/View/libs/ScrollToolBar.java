package com.eki.parking.View.libs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eki.parking.R;
import com.eki.parking.Controller.listener.AppBarStateChangeListener;

import com.google.android.material.appbar.AppBarLayout;
import com.hill.devlibs.tools.Log;

/**
 * Created by Hill on 2018/9/28.
 */
public class ScrollToolBar extends Toolbar {

    private AppBarLayout parentView;
    private Context mContext;
    private ImageView iconImg;
    private Drawable icon;

    public ScrollToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public ScrollToolBar initView(AppBarLayout parent) {
        parentView = parent;
        setLayoutDirection(ViewGroup.LAYOUT_DIRECTION_RTL);
        parentView.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                Log.d("ScrollToolBar state->"+state.name());
                switch (state) {
                    case EXPANDED:
                    case IDLE:
//                        if (iconImg != null)
//                            iconImg.setImageResource(R.this.icon_arrow_right_black);
//                        else
                        setLogo(null);

                        break;
                    default:
//                        if (iconImg != null)
//                            iconImg.setImageResource(R.this.icon_arrow_right_light_gray);
//                        else
                        setLogo(R.drawable.icon_arrow_down_gray);
                }
            }
        });
        return this;
    }

//    public ScrollToolBar setIcon(@DrawableRes int res) {
//        icon = mContext.getDrawable(res);
//        return this;
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        PrintLogKt.w("ScrollToolBar w->" + widthMeasureSpec + " h->" + heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        PrintLogKt.d("ScrollTooBar onLayout changer->" + changed + " l->" + l + " t->" + t + " r->" + r + " b->" + b);
//        int lenght = b * 7 / 10;
//
//        if (iconImg == null)
//            iconImg = initIcon(lenght);
//        iconImg.setImageDrawable(icon);

//        layoutChildRight(iconImg, l, t, r, b);

//        iconImg.layout();

//        iconImg.setLayoutParams(getIconParams(lenght));

    }

    private void layoutChildRight(View child, int l, int t, int r, int b) {
        int width = child.getLayoutParams().width;
        int height = child.getLayoutParams().height;

        int top = (b - height) / 2;
        int left = (r - width);
        int right = r;
        int bottom = b - (top + height);
        child.layout(left, top, right, bottom);
//        addView(child);
    }


//    private int layoutChildRight(View child, int right, int[] collapsingMargins,
//                                 int alignmentHeight) {
//        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
//        final int r = lp.rightMargin - collapsingMargins[1];
//        right -= Math.max(0, r);
//        collapsingMargins[1] = Math.max(0, -r);
//        final int top = getChildTop(child, alignmentHeight);
//        final int childWidth = child.getMeasuredWidth();
//        child.layout(right - childWidth, top, right, top + child.getMeasuredHeight());
//        right -= childWidth + lp.leftMargin;
//        return right;
//    }

    private ImageView initIcon(int lenght) {
        ImageView img = new AppCompatImageView(mContext);
        LayoutParams lp = new LayoutParams(lenght, lenght);
        img.setLayoutParams(lp);
        return img;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//        PrintLogKt.i("ScrollToolBar dispatchDraw");

    }
}
