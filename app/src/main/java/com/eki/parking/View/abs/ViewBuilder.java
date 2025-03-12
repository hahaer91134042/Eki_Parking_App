package com.eki.parking.View.abs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eki.parking.App;
import com.hill.devlibs.impl.InflatViewImpl;
import com.hill.devlibs.impl.LayoutParamsImpl;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

/**
 * Created by Hill on 2018/10/23.
 */
public abstract class ViewBuilder implements InflatViewImpl, LayoutParamsImpl {
    public View itemView;
    public Context context;
    public String TAG=getClass().getSimpleName();
    public ViewBuilder(Context c){
//        itemView=view;
        context=c;
        itemView=LayoutInflater.from(context).inflate(setInflatView(),null,setAttachToRoot());
    }

    protected boolean setAttachToRoot() {
        return false;
    }
    @Override
    public ViewGroup.LayoutParams initNewLayoutParams() {
        return null;
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
