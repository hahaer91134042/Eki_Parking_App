package com.hill.devlibs.dialog;


import com.hill.devlibs.BaseApp;
import com.hill.devlibs.activity.LibBaseActivity;
import com.hill.devlibs.frag.LibBaseFragment;

/**
 * Created by Hill on 2018/11/9.
 */
public abstract class LibDialogFrag<Frag extends LibDialogFrag,
                                    APP extends BaseApp> extends LibBaseFragment<Frag,APP> {
    public OnChildStateListener stateListener;

    protected void dissmissDialog(){
        if (stateListener !=null)
            stateListener.onChildDissmiss();
    }
    protected void dialogBack(){
        if (stateListener!=null)
            stateListener.onChildBack();
    }

    public Frag setStateListener(OnChildStateListener listener){
        if (listener != null) {
            stateListener=listener;
        }
        return (Frag)this;
    }

    public interface OnChildStateListener {
        void onChildDissmiss();
        void onChildBack();
    }
}
