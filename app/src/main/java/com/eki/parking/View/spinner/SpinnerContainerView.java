package com.eki.parking.View.spinner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.eki.parking.R;
import com.eki.parking.Controller.util.ScreenUtils;
import com.eki.parking.View.abs.LinearCustomView;
import com.eki.parking.View.impl.ISpinnerContainer;

/**
 * Created by Hill on 2018/5/10.
 */
public class SpinnerContainerView extends LinearCustomView {

    public Spinner spinner;
    public ImageView imgView;

    public SpinnerContainerView(Context context) {
        this(context,null);
    }

    public SpinnerContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setBackground(context.getDrawable(R.drawable.stroke_gray2));
//        initView();
    }

    public void initView(Spinner spinner,Boolean useCustom){
        if(useCustom){
            initView(spinner);
        }else {
            setBackground(null);
            this.spinner=spinner;
            spinner.setLayoutParams(getDefaultParams());
//            spinner.setGravity(Gravity.CENTER_VERTICAL);
//            spinner.setBackgroundResource(android.R.color.transparent);
            addView(spinner);
        }
    }

    public void initContainer(ISpinnerContainer impl){
        impl.containerSet(this);
        spinner=impl.getSpinner();
        addView(spinner);
        imgView=impl.getIcon();
        imgView.setOnClickListener(v -> {
            spinner.performClick();
        });
        addView(imgView);
    }

    public void initView(Spinner spinner) {
//        catalogSpinner=new SubCatalogSpinner(context);
        this.spinner=spinner;
        spinner.setLayoutParams(getSpinnerParams());
        spinner.setGravity(Gravity.CENTER_VERTICAL);
        spinner.setBackgroundResource(android.R.color.transparent);
        addView(spinner);
        imgView=new ImageView(context);
        imgView.setLayoutParams(getImgParams());
        imgView.setImageResource(R.drawable.icon_drop_down);
        imgView.setOnClickListener(v -> {
            spinner.performClick();
        });
        addView(imgView);
    }

    public void initView(Spinner spinner,LayoutParams spinnerParams) {
//        catalogSpinner=new SubCatalogSpinner(context);
        this.spinner=spinner;
        if (spinnerParams!=null)
            spinner.setLayoutParams(spinnerParams);

        spinner.setGravity(Gravity.CENTER_VERTICAL);
        spinner.setBackgroundResource(android.R.color.transparent);
        addView(spinner);
        imgView=new ImageView(context);
        imgView.setLayoutParams(getImgParams());
        imgView.setImageResource(R.drawable.icon_drop_down);
        imgView.setOnClickListener(v -> {
            spinner.performClick();
        });
        addView(imgView);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        spinner.setEnabled(enabled);
        imgView.setEnabled(enabled);
    }

    private LinearLayout.LayoutParams getImgParams() {
        int margin=ScreenUtils.dpToPx(2.5f);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(
                ScreenUtils.getScreenWidth()*7/100,
                ScreenUtils.getScreenWidth()*7/100
        );
        lp.gravity=Gravity.CENTER_VERTICAL;
        lp.setMargins(margin,0,margin,0);
        return lp;
    }

    private LinearLayout.LayoutParams getSpinnerParams() {
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        lp.gravity=Gravity.CENTER_VERTICAL;
        return lp;
    }

    private LinearLayout.LayoutParams getDefaultParams(){
        return new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
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
