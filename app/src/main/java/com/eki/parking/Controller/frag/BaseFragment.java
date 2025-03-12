package com.eki.parking.Controller.frag;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eki.parking.App;
import com.eki.parking.R;
import com.eki.parking.Controller.dialog.MsgDialog;
import com.eki.parking.Controller.manager.SQLManager;

import com.eki.parking.Controller.tools.GPS;
import com.hill.devlibs.frag.LibBaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Hill on 2017/5/24.
 */

public abstract class BaseFragment<Frag extends BaseFragment>
        extends LibBaseFragment<Frag,App> {

    //protected int childFragLoaderRes=R.id.fragViewLoader;


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        PrintLogKt.e(TAG,"Frag->onCreateView");
        return  super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected int getChildFragLoader(){
        return R.id.fragViewLoader;
    }

    protected void showErrorMsgDialog(String message){
        new MsgDialog(getContext())
                .setPositiveBtn(getString(R.string.Determine))
                .setShowTitle(getString(R.string.Error_Message))
                .setShowMsg(message)
                .show();
    }


    public SQLManager getSqlManager(){
        return getApp().getSqlManager();
    }




    //雖然這樣有點怪 但是不這樣做的話 子代使用到getApp出來會是BaseAPP 這可能是java泛型的問題
    @Override
    protected App getApp() {
        return App.getInstance();
    }
    protected GPS getGps(){
        return getApp().getGps();
    }
    @Override
    protected int getWidth() {
        return App.getScreenWidth();
    }

    @Override
    protected int getHeight() {
        return App.getScreenHeight();
    }
    //    @Override
//    protected App getApp() {
//        return super.getApp();
//    }

    //    protected class ViewFinder<V extends View>{
//        V[] views;
//        int[] resArr;
//        public ViewFinder(V... vs){
//            views=vs;
//        }
//        public ViewFinder setRes(@IdRes int... arr){
//            resArr=arr;
//            return this;
//        }
//        public void find(){
//            for (int i = 0; i <views.length ; i++) {
//                views[i]=getView().findViewById(resArr[i]);
//            }
//        }
//    }
}
