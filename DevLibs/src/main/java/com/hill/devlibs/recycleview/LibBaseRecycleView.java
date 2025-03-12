package com.hill.devlibs.recycleview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;


import com.hill.devlibs.BaseApp;
import com.hill.devlibs.IntentFlag;
import com.hill.devlibs.model.ValueObjContainer;

import androidx.annotation.ArrayRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Hill on 2017/12/1.
 */
public abstract class LibBaseRecycleView<APP extends BaseApp> extends RecyclerView {
    protected Context context;

//    public LibBaseRecycleView(Context context) {
//        this(context,null);
//    }
    public void useVerticalView(){
        setLayoutManager(new LinearLayoutManager(context));
        setHasFixedSize(true);
    }

    public void useSimpleDivider(){
        useVerticalView();
        addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.DividerColor.LightGray));
    }
    public void useDrawableDivider(@DrawableRes int res){
        useVerticalView();
        addItemDecoration(new DividerItemDecoration(context,res));
    }

    public LibBaseRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }
    protected void sendBroadCast(String action, ValueObjContainer container) {
        Intent intent=new Intent(action);
        intent.putExtra(IntentFlag.DATA_FLAG,container);
        sendBroadCast(intent);
    }
    protected void sendBroadCast(Intent... intents){
        for (Intent intent:
             intents) {
            context.sendBroadcast(intent);
        }
    }
    protected String[] getArray(@ArrayRes int res){
        return context.getResources().getStringArray(res);
    }
    protected String getString(@StringRes int res){
        return context.getString(res);
    }
    protected abstract int getScreenHeight();
    protected abstract int getScreenWidth();
    protected abstract APP getApp();
}
