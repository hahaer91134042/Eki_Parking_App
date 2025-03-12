package com.hill.devlibs.recycleview.item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.Toast;


import com.hill.devlibs.impl.InflatViewImpl;
import com.hill.devlibs.listener.ItemClickListener;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Hill on 2017/4/5.
 */

public abstract class LibItemLayout<VO>extends RecyclerView.ViewHolder{
    protected String TAG=getClass().getSimpleName();
    public View itemView;
    protected int lenght = 0;
    protected Context context;
//    public int position;
    private ItemClickListener listener;
    public VO itemData;

    public LibItemLayout(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        context=itemView.getContext();
    }

    public void init(){}
    public void init(int lenght){
        this.lenght=lenght;
    }
//    public void refresh(String name){
//    }
    public void refresh(VO data){
        itemData=data;
    }
//    public void setPosition(int p){
//        position=p;
//    }

    protected void onCatchReceiver(String action,Intent intent) {
    }
    public void onRecycle() {
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onCatchReceiver(intent.getAction(),intent);
        }
    };

    protected void registerReceiver(String... actions){
        IntentFilter filter=new IntentFilter();
        for (String action:
             actions) {
            filter.addAction(action);
        }
        context.registerReceiver(mReceiver,filter);
    }

    protected void unRegisterReceiver(){
        try {
            context.unregisterReceiver(mReceiver);
        }catch (Exception e){

        }
    }

    protected void sendBroadcast(Intent... intents) {
        for (Intent intent :
                intents) {
            context.sendBroadcast(intent);
        }
    }

    protected void sendBroadcast(String... actions) {
        for (String act :
                actions) {
            context.sendBroadcast(new Intent(act));
        }
    }

    protected void sendBroadcast(Context context, String... actions) {
        for (String act :
                actions) {
            context.sendBroadcast(new Intent(act));
        }
    }

    protected int getColor(@ColorRes int res){
        return context.getResources().getColor(res);
    }
    protected void showToast(String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }

    protected String getString(@StringRes int res){
        return context.getString(res);
    }

    public void setItemClickListener(ItemClickListener l) {
        listener=l;
    }
    protected void itemClick(){
        if (listener!=null)
            listener.onItemClick(getLayoutPosition());

    }

}
