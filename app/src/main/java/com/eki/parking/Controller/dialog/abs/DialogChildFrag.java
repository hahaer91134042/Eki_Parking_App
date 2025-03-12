package com.eki.parking.Controller.dialog.abs;

import com.eki.parking.App;
import com.eki.parking.R;
import com.eki.parking.Controller.dialog.MsgDialog;
import com.eki.parking.Controller.util.ScreenUtils;
import com.hill.devlibs.dialog.LibDialogFrag;

/**
 * Created by Hill on 2018/11/9.
 */
public abstract class DialogChildFrag<Frag extends DialogChildFrag> extends LibDialogFrag<Frag, App> {


    @Override
    protected void showErrorMsgDialog(String message) {
        new MsgDialog(getContext())
                .setPositiveBtn(getString(R.string.Determine))
                .setShowTitle(getString(R.string.Error_Message))
                .setShowMsg(message)
                .show();
    }

    @Override
    protected int getChildFragLoader() {
        return R.id.fragViewLoader;
    }

    @Override
    protected App getApp() {
        return App.getInstance();
    }

    @Override
    protected int getWidth() {
        return ScreenUtils.getScreenWidth();
    }

    @Override
    protected int getHeight() {
        return ScreenUtils.getScreenHeight();
    }
}
