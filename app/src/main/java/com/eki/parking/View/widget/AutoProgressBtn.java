package com.eki.parking.View.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.eki.parking.R;
import com.eki.parking.View.abs.FrameCustomView;

/**
 * Created by Hill on 2018/11/13.
 */
public class AutoProgressBtn extends FrameCustomView {

    private int textColor=Color.BLACK;
    private float textSize=15f;
    private int textRes=0;
    private String btnText="";
    private int btnImg=R.drawable.none_img,refreshImg=0;
    private Context context;
    public Button clickBtn,refreshBtn;
//    private ProgressBar progressBar;

    public AutoProgressBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.AutoProgressBtn);
        parse(ta);
        initView();
    }

    private void initView() {
        ProgressBar progressBar=new ProgressBar(context);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        addView(progressBar);

        refreshBtn=getButton();
        if (refreshImg!=0)
            refreshBtn.setBackground(context.getDrawable(refreshImg));

        clickBtn=getButton();
        clickBtn.setBackground(context.getDrawable(btnImg));
        if (textRes!=0)
            btnText=context.getString(textRes);

        clickBtn.setText(btnText);
        clickBtn.setTextColor(textColor);
        clickBtn.setTextSize(textSize);
    }

    private Button getButton() {
        Button btn=new Button(context);
        btn.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        return btn;
    }

    private void parse(TypedArray typedArray) {
        textRes=typedArray.getResourceId(R.styleable.AutoProgressBtn_afterProgressText,textRes);
        btnText=typedArray.getString(R.styleable.AutoProgressBtn_afterProgressText);
        btnImg=typedArray.getResourceId(R.styleable.AutoProgressBtn_afterProgressImg,btnImg);
        refreshImg=typedArray.getResourceId(R.styleable.AutoProgressBtn_afterProgressRefresh,refreshImg);
        textColor=typedArray.getColor(R.styleable.AutoProgressBtn_btn_textColor,textColor);
        textSize=typedArray.getDimension(R.styleable.AutoProgressBtn_btn_textSize,textSize);
        typedArray.recycle();
    }

    public void onProgress(){
        removeAllViews();
        ProgressBar progressBar=new ProgressBar(context);
        progressBar.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        addView(progressBar);
    }

    public void prepareFinish(){
        removeAllViews();
        addView(clickBtn);
    }

    public void onFail(){
        removeAllViews();
        addView(refreshBtn);
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
