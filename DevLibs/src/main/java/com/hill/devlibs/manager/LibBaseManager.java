package com.hill.devlibs.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.hill.devlibs.BaseApp;
import com.hill.devlibs.tools.Log;

import androidx.annotation.NonNull;


/**
 * Created by Hill on 2017/10/11.
 */

public abstract class LibBaseManager {

    protected String TAG=getClass().getSimpleName();
    protected Context context;

    public LibBaseManager(@NonNull Context context){
        this.context=context;
    }

    private BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onCatchReceive(intent.getAction(),intent);
        }
    };

    protected void onCatchReceive(String action,Intent intent) {
    }

    protected void sendBroadcast(String... actions) {
        for (String action:
             actions) {
            context.sendBroadcast(new Intent(action));
        }
    }
    protected void sendBroadcast(Intent... intents) {
        for (Intent intent:
                intents) {
            context.sendBroadcast(intent);
        }
    }

    protected void registerFlag(String... flags){
        IntentFilter filter=new IntentFilter();
        for (String flag:
             flags) {
            filter.addAction(flag);
        }
        context.registerReceiver(mReceiver,filter);
    }
    protected void unRegister() {
        try {
            context.unregisterReceiver(mReceiver);
        }catch (Exception e){

        }
    }

    protected String getString(int res){
        return context.getString(res);
    }
    protected String getPackageName(){
        return context.getPackageName();
    }
    protected String[] getStringArr(int res){
        return context.getResources().getStringArray(res);
    }
    protected void printLog(String msg){
        Log.d(TAG,msg);
    }
    protected void printException(Exception e){
        Log.e(TAG,e.toString());
    }
}
