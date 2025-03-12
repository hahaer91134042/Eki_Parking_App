package com.eki.parking.View.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eki.parking.R;

import com.eki.parking.Controller.util.ScreenUtils;
import com.eki.parking.View.abs.LinearCustomView;

/**
 * Created by Hill on 2018/8/24.
 */
public class CharFastSearchBar extends LinearCustomView
                               implements View.OnClickListener{
//                                          View.OnTouchListener{

    float textSize=10f;
    int textColor= Color.BLACK;

    private CharCkickListener listener;



    public interface CharCkickListener{
        void onCharSelect(String key);
    }

    public CharFastSearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setBackground(null);
        parseAttrs(attrs);

//        initOption();
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray attrsArray=context.obtainStyledAttributes(attrs,R.styleable.CharFastSearchBar);
        textSize=attrsArray.getDimension(R.styleable.CharFastSearchBar_searchTextSize,textSize);
        textColor=attrsArray.getColor(R.styleable.CharFastSearchBar_searchTextColor,textColor);
    }

    // TODO:Edit by Hill 2018/8/24 這邊監聽要蓋在其他的list選項上面 所以要比較晚設定監聽 不然會被蓋掉 
    public void initOption() {
        String[] optionArr=context.getResources().getStringArray(R.array.fastSearchKey);
        for (String option:
             optionArr) {
            TextView opText=new TextView(context);
            opText.setLayoutParams(optionTextParams());
            opText.setGravity(Gravity.CENTER);
            opText.setText(option);
            opText.setTextSize(textSize);
            opText.setTextColor(textColor);
            opText.setOnClickListener(this);
//            opText.setOnTouchListener(this);
            addView(opText);
        }
    }

    public CharFastSearchBar setCharSelectListener(CharCkickListener l){
        listener=l;
        return this;
    }

    private LinearLayoutCompat.LayoutParams optionTextParams(){
        return new LinearLayoutCompat.LayoutParams(
                ScreenUtils.dpToPx(30f),
                ScreenUtils.dpToPx(25f)
        );
    }

    @Override
    public ViewGroup.LayoutParams initNewLayoutParams() {
        return new LinearLayoutCompat.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    public int setInflatView() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView){
            if (listener!=null)
                listener.onCharSelect(((TextView)v).getText().toString());
        }
    }
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                PrintLogKt.d(TAG+" action down");
//                if (usageListener!=null)
//                    usageListener.onCharSelect(((TextView)v).getText().toString());
//                break;
//        }
//
//        return true;
//    }

//    float x;
//    float y;
//
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                PrintLogKt.i( "button按下");
//                //记录按下时的位置
//                x = motionEvent.getRawX();
//                y = motionEvent.getRawY();
//                break;
//            }
//            case MotionEvent.ACTION_MOVE: {
//                PrintLogKt.i( "button移动");
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                //检测移动的距离，如果很微小可以认为是点击事件
//                if (Math.abs(motionEvent.getRawX() - x) < 20 && Math.abs(motionEvent.getRawY() - y) < 20) {
//                    try {
//                        Field field = View.class.getDeclaredField("mListenerInfo");
//                        field.setAccessible(true);
//                        Object object = field.get(view);
//                        field = object.getClass().getDeclaredField("mOnClickListener");
//                        field.setAccessible(true);
//                        object = field.get(object);
//                        if (object != null && object instanceof View.OnClickListener) {
//                            ((View.OnClickListener) object).onClick(view);
//                        }
//                    } catch (Exception e) {
//
//                    }
//                } else {
//                    PrintLogKt.i( "button已移动");
//                }
//                break;
//            }
//        }
//        return true;
//    }

}
