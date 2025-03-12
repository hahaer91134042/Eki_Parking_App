package com.hill.devlibs.dialog;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;


import com.hill.devlibs.BaseApp;
import com.hill.devlibs.EnumClass.DialogOption;
import com.hill.devlibs.IntentFlag;
import com.hill.devlibs.frag.FragController;
import com.hill.devlibs.frag.FragController.FragSwitcher;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/**
 * Created by Hill on 2017/10/20.
 */

public abstract class LibBaseFragDialog<ChildFrag extends LibDialogFrag,
                                        APP extends BaseApp> extends DialogFragment
                                                             implements LibDialogFrag.OnChildStateListener {

    public String TAG=getClass().getSimpleName();
    private FragController fragController;
    private DialogSetting setting;
    private @IdRes int fragContainer= setDefaultFragContainer();
    protected ChildFrag initFrag;
    protected View parentView;

    Bundle savedState;
    private String bundleKey="internalSavedViewState8954201239547";

    protected class DialogSetting{
        public DialogOption[] options;
        public DialogSetting(DialogOption... ops){
            options=ops;
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToArguments();
    }
    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle argument=getArguments();
            if (argument==null)
                argument=new Bundle();

            argument.putBundle(bundleKey, savedState);
            try {
                /**
                 * When activity destory that fragment attached.
                 * This method will be throw Exception.
                 * So,it will be ignore.
                 */
                setArguments(argument);
            }catch (IllegalStateException e){
            }
            savedState=null;
        }
    }
    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
//        Log.e("restoreArguments->"+b);
        if (b!=null && b.containsKey(bundleKey))
            savedState = b.getBundle(bundleKey);

        if (savedState != null) {
            return true;
        }
        return false;
    }
    private Bundle saveState() {
        Bundle state = new Bundle();
        return onSaveState(state);
    }

    protected Bundle onSaveState(@NonNull Bundle outState) {

        return outState;
    }

    //------------Life cycle-------------
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerReceiver(IntentFlag.Dissmiss);
        fragController=new FragController(getChildFragmentManager());

        initFrag=initDialogViewFrag();
//        Log.w(TAG+" initFrag->"+initFrag);
        if (initFrag!=null){
            initFrag.stateListener =this;
            replaceFragment(initFrag,initFrag.TAG,null, FragSwitcher.SWITCH_FADE);
        }

        if (!restoreStateFromArguments()){
            setUpParentDialogComponent();
        }else {
            onRestartViewState(savedState);
        }
    }
    protected void onRestartViewState(Bundle savedState) {
    }
    protected abstract DialogSetting setUpDialogFeature();
    protected abstract @Nullable ChildFrag initDialogViewFrag();
    protected abstract int setParentViewInflate();
    protected abstract void setUpParentDialogComponent();

    @Override
    public void onChildDissmiss() {
        dismiss();
    }

    @Override
    public void onChildBack() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setting=setUpDialogFeature();
        Dialog dialog=getDialog();
        Window window=dialog.getWindow();

        if (setting!=null){
            for (DialogOption op:
                    setting.options) {
                switch (op){
                    case NoTitle:
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        break;
                    case CantCancelable:
                        dialog.setCanceledOnTouchOutside(false);
                        setCancelable(false);
                        break;
                    case Colorless:
                        window.setBackgroundDrawableResource(android.R.color.transparent);
                        break;
                    case NoDim:
                        window.setDimAmount(0);
                        break;
                    case ScreenBottom:
                        window.setGravity(Gravity.BOTTOM);
                        break;
                    case ScreenTop:
                        window.setGravity(Gravity.TOP);
                        break;
                }
            }
        }
        parentView=inflater.inflate(setParentViewInflate(),container,false);
        return parentView;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (setting!=null){
            for (DialogOption option:
                 setting.options) {
                switch (option){
                    case FullWidth:
                        getDialog().getWindow().setLayout(
                                getScreenWidth(),
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        break;
                    case FullHeight:
                        getDialog().getWindow().setLayout(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                getScreenHeight()
                        );
                        break;
                    case FullScreen:
                        getDialog().getWindow().setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        );
//                        getDialog().getWindow().setLayout(
//                                getScreenWidth(),
//                                getScreenHeight()
//                        );
                        //*96/100
                        break;
                    case WrapContent:
                        getDialog().getWindow().setLayout(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        break;
                    case FullDialogWidth:
                        getDialog().getWindow().setLayout(
                                (int)(getScreenWidth()*0.8),
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        break;
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroyView() {
        saveStateToArguments();
        unRegisterReceiver();
        super.onDestroyView();
    }
    //----------------------------------
    protected BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(IntentFlag.Dissmiss)){
                dismiss();
            }else {
                onCatchIntent(intent.getAction(),intent);
            }
        }
    };

    protected void onCatchIntent(String action, Intent intent) {
    }

    protected void registerReceiver(String... flags){
        IntentFilter filter=new IntentFilter();
        for (String flag:
                flags) {
            filter.addAction(flag);
        }
        getContext().registerReceiver(mReceiver,filter);
    }
    private void unRegisterReceiver(){
        try {
            getContext().unregisterReceiver(mReceiver);
        }catch (Exception e){

        }
    }
    protected void sendBroadcast(String... actions){
        for (String act:
                actions) {
            getActivity().sendBroadcast(new Intent(act));
        }
    }

    protected abstract @IdRes int setDefaultFragContainer();

//    protected void setFragContainer(@IdRes int idRes){
//        fragContainer=idRes;
//    }

    protected void replaceFragment(ChildFrag fragment, String targetTag, String backTag, FragSwitcher switchType) {
        if (fragment!=null)
            fragment.stateListener=this;
        fragController.replaceFragment(fragContainer, fragment, targetTag, backTag, switchType);
    }

    protected abstract int getScreenHeight();
    protected abstract int getScreenWidth();
    protected abstract APP getApp();

    protected void showToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
    }
}
