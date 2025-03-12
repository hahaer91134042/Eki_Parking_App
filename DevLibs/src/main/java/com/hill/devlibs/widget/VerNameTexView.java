package com.hill.devlibs.widget;

import android.content.Context;
import android.util.AttributeSet;


import com.hill.devlibs.tools.MobileInfo;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Hill on 2017/11/14.
 */

public class VerNameTexView extends AutoSizeTextView {
    private Context context;
//    private View itemView;
//    private TextView verTex;
    public VerNameTexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
//        LayoutInflater inflater=LayoutInflater.from(context);
//        itemView=inflater.inflate(R.layout.item_textview,this,false);
        initView();
    }

    private void initView() {
//        verTex=(TextView)itemView.findViewById(R.id.textView);
//        verTex.setText(getAppVersionName());
//        addView(verTex);
        setText(getAppVersionName());
    }
    public void setColor(int color){
        setTextColor(color);
    }

    private String getAppVersionName() {
        return MobileInfo.appVersionFrom(context);
    }
}
