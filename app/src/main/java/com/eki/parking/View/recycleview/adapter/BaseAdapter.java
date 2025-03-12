package com.eki.parking.View.recycleview.adapter;

import android.content.Context;

import com.eki.parking.App;
import com.eki.parking.View.recycleview.item.ItemLayout;
import com.hill.devlibs.listener.ItemClickListener;
import com.hill.devlibs.recycleview.adapter.LibBaseAdapter;


/**
 * Created by Hill on 2017/12/1.
 */

public abstract class BaseAdapter<VH extends ItemLayout> extends LibBaseAdapter<VH,App> {


    public BaseAdapter(Context context){
        super(context);
    }


    @Override
    public void onBindViewHolder(VH item, int position) {
        super.onBindViewHolder(item,position);
    }

    @Override
    public void setItemListClickListener(ItemClickListener l) {
        super.setItemListClickListener(l);
    }

    @Override
    protected int getWidth() {
        return App.getScreenWidth();
    }
    @Override
    protected int getHeight() {
        return App.getScreenHeight();
    }
    @Override
    public App getApp(){
        return App.getInstance();
    }
}
