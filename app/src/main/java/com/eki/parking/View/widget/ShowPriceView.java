package com.eki.parking.View.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eki.parking.R;
import com.eki.parking.Controller.util.ScreenUtils;
import com.eki.parking.View.abs.LinearCustomView;
import com.hill.devlibs.EnumClass.SpanHelper;
import com.hill.devlibs.util.StringUtil;

/**
 * Created by Hill on 2018/10/19.
 */
public class ShowPriceView extends LinearCustomView {

    TextView oldPriceText;
    TextView newPriceText;

    public ShowPriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        initView();
    }

    private void initView() {
        oldPriceText=new TextView(context);
        oldPriceText.setLayoutParams(getTextLayoutParams());
        oldPriceText.setTextColor(Color.GRAY);
        oldPriceText.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        oldPriceText.setTextSize(ScreenUtils.dpToPx(7));
        addView(oldPriceText);

        newPriceText=new TextView(context);
        newPriceText.setLayoutParams(getTextLayoutParams());
        newPriceText.setTextColor(Color.RED);
        newPriceText.setGravity(Gravity.CENTER);
        newPriceText.setTextSize(ScreenUtils.dpToPx(7));
        addView(newPriceText);

    }

    public ShowPriceView setTextSize(float size){
        oldPriceText.setTextSize(size);
        newPriceText.setTextSize(size);
        return this;
    }

    public ShowPriceView setPrice(long origin,long special){

        String ori= StringUtil.getFormateMessage(getString(R.string.NT_price_string),origin);
        String spe="";
        if (special>0)
            spe=StringUtil.getFormateMessage(getString(R.string.NT_price_string),special);


        if (!StringUtil.isEmptyString(spe)){
            StringUtil.getSpanBuilder()
                    .setSpanString(ori, SpanHelper.TYPE_ITALIC.hasStrikeSpan(true).setTextColor(Color.GRAY).setRelativeSize(0.8f))
                    .into(oldPriceText);

//            oldPriceText.setText(ori);
            newPriceText.setText(spe);
        }else{
            oldPriceText.setText("");
            newPriceText.setText(ori);
        }

        return this;
    }


    private LinearLayout.LayoutParams getTextLayoutParams() {
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        lp.weight=1;
        return lp;
    }

    @Override
    public ViewGroup.LayoutParams initNewLayoutParams() {
        return null;
    }

    @Override
    public int setInflatView() {
        return 0;
    }
}
