package com.eki.parking.View.abs;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.eki.parking.App;

/**
 * Created by Hill on 2017/11/21.
 */

public abstract class RelativeCustomView extends RelativeLayout
                                         implements CustomViewFeature {
    protected String TAG=getClass().getSimpleName();
    protected LayoutInflater inflater;
    protected Context context;
    protected View itemView;



    public RelativeCustomView(Context context) {
        this(context,null);
    }

    public RelativeCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        inflater=LayoutInflater.from(context);
        parseAttrSet(attrs);
        if (initNewLayoutParams()!=null){
            setLayoutParams(initNewLayoutParams());
        }

        if (setInflatView()!=0){
            itemView=inflater.inflate(setInflatView(),this,setAttachToRoot());
            initInFlaterView();
        }
    }

    protected void initInFlaterView() {
    }

    private boolean isInit=true;
    private TypedArray typedArray;
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isInit){
            setUpInflatView(typedArray);
            if(typedArray!=null)
                typedArray.recycle();
            isInit=false;
        }
    }
    protected void setUpInflatView(TypedArray typedArray) {
    }



    private void parseAttrSet(AttributeSet attrs) {
        if (setStyleableRes()!=null){
            typedArray=context.obtainStyledAttributes(attrs,setStyleableRes());
            parseTypedArray(typedArray);
//            typedArray.recycle();
        }
    }
    @Override
    public int[] setStyleableRes() {
        return null;
    }

    @Override
    public void parseTypedArray(@NonNull TypedArray typedArray) {
    }
    protected boolean setAttachToRoot() {
        return true;
    }
    public int getColor(@ColorRes int res){
        return context.getResources().getColor(res);
    }

    public String getString(@StringRes int res){
        return context.getString(res);
    }
    public App getApp(){
        return App.getInstance();
    }

}
