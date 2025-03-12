package com.hill.devlibs.recycleview.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hill.devlibs.BaseApp;
import com.hill.devlibs.R;
import com.hill.devlibs.listener.ItemClickListener;
import com.hill.devlibs.listener.ItemClickListener2_bak;
import com.hill.devlibs.recycleview.item.LibItemLayout;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.ArrayRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;


/**
 * Created by Hill on 2017/12/1.
 */

public abstract class LibBaseAdapter<VH extends LibItemLayout,
                                     APP extends BaseApp> extends RecyclerView.Adapter<VH> {
    protected String TAG=getClass().getSimpleName();
    protected Context context;
    protected LayoutInflater inflater;
    private ItemClickListener listener;


    public LibBaseAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public void onBindViewHolder(VH item, int position) {
        listenerItemClick(item);
//        item.setPosition(position);
    }

    @Override
    public void onViewRecycled(@NonNull VH holder) {
        holder.onRecycle();
    }


    public void setItemListClickListener(ItemClickListener l){
        listener=l;
    }
//    protected ItemClickListener2_bak lll;
//    public void setTestListener(ItemClickListener2_bak l){
//        lll=l;
//        lll.onItemClick(1);
//    }
//    public void setTestLis(ItemClickListener2_bak ll){
//
//    }

    protected View getItemView(@LayoutRes int res,ViewGroup parent){
        return inflater.inflate(res, parent,false);
    }

    public void listenerItemClick(VH item){
        if (listener!=null)
            item.setItemClickListener(listener);
    }

    protected String[] getStringArr(@ArrayRes int res){
        return context.getResources().getStringArray(res);
    }
    protected void sendBroadCast(String... actions){
        for (String action:
             actions) {
            context.sendBroadcast(new Intent(action));
        }
    }

    protected String getString(int res){
        return context.getString(res);
    }

    protected abstract int getWidth();
    protected abstract int getHeight();
    protected abstract APP getApp();

    protected <VO> ErrorItem<VO> getErrorItem(ViewGroup parent){
        View view=inflater.inflate(R.layout.item_title_textview,parent,false);
        return new ErrorItem<>(view);
    }

    protected <VO> EmptyItem<VO> getEmptyItem(ViewGroup parent){
        View view=inflater.inflate(R.layout.item_textview,parent,false);
        return new EmptyItem<>(view);
    }


    protected class EmptyItem<VO> extends LibItemLayout<VO> {

        public TextView tex;
        public EmptyItem(View itemView) {
            super(itemView);
            tex=itemView.findViewById(R.id.textView);
            tex.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            tex.setGravity(Gravity.CENTER);
            tex.setTextSize(context.getResources().getDimension(R.dimen.text_size_16));
            tex.setTextColor(Color.BLACK);
            tex.setBackgroundColor(getColor(R.color.light_gray2));
        }


        public EmptyItem<VO> setMsg(String msg){
            tex.setText(msg);
            return this;
        }
    }

    protected class ErrorItem<VO> extends LibItemLayout<VO> {
        public TextView tex;
        public ErrorItem(View itemView) {
            super(itemView);
            tex=itemView.findViewById(R.id.textView);
            tex.setGravity(Gravity.CENTER);
            tex.setText(getString(R.string.error));
        }

    }
}
