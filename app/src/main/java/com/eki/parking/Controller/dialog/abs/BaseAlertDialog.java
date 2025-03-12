package com.eki.parking.Controller.dialog.abs;

import android.content.Context;

import com.eki.parking.App;
import com.hill.devlibs.dialog.LibBaseAlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by Hill on 2018/11/14.
 */
public abstract class BaseAlertDialog extends LibBaseAlertDialog<App> {



    public BaseAlertDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected int getScreenHeight() {
        return App.getScreenHeight();
    }

    @Override
    protected int getScreenWidth() {
        return App.getScreenWidth();
    }

    protected App getApp(){
        return App.getInstance();
    }

//    @Override
//    public AlertDialog show() {
//        return super.show();
//    }


    @Override
    public AlertDialog show() {
        return super.show();
    }
}
