package com.hill.viewpagerlib.transformer;

import android.os.Build;
import androidx.viewpager.widget.ViewPager;
import android.view.View;

//import com.hhl.tubatu.Log;

/**
 * Created by HanHailong on 15/9/27.
 */
public class ScalePageTransformer implements ViewPager.PageTransformer {

    private static final float MAX_SCALE = 1.2f;
    private static final float MIN_SCALE = 0.6f;

    @Override
    public void transformPage(View page, float position) {
//        Log.i(getClass().getSimpleName(),"position->"+position+" view->"+page);

        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;
//        Log.d("tempScale->"+tempScale);
        float slope = (MAX_SCALE - MIN_SCALE) / 1;
//        Log.e("slope->"+slope);
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
//        Log.w("scaleValue->"+scaleValue);
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
}
