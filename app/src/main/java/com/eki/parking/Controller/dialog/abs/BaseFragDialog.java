package com.eki.parking.Controller.dialog.abs;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eki.parking.App;
import com.eki.parking.R;
import com.hill.devlibs.dialog.LibBaseFragDialog;
import com.hill.devlibs.frag.FragController;


/**
 * Created by Hill on 2017/10/20.
 */

public abstract class BaseFragDialog<Frag extends DialogChildFrag>
        extends LibBaseFragDialog<Frag,App>{


    //------------Life cycle-------------
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected int setDefaultFragContainer() {
        return R.id.dialog_content_loader;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater,container,savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void replaceFragment(Frag fragment, String targetTag, String backTag, FragController.FragSwitcher switchType) {
        super.replaceFragment(fragment, targetTag, backTag, switchType);
    }

    public App getApp(){
        return App.getInstance();
    }

    @Override
    protected int getScreenHeight() {
        return App.getScreenHeight();
    }

    @Override
    protected int getScreenWidth() {
        return App.getScreenWidth();
    }

}
