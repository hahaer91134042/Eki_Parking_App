package com.hill.devlibs.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.hill.devlibs.BaseApp;
import com.hill.devlibs.EnumClass.BtnEnum;
import com.hill.devlibs.EnumClass.DialogOption;
import com.hill.devlibs.impl.InflatViewImpl;
import com.hill.devlibs.impl.InitViewImpl;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by Hill on 2018/11/14.
 */
public abstract class LibBaseAlertDialog<APP extends BaseApp> extends AlertDialog.Builder
        implements InflatViewImpl,
        InitViewImpl {

    protected Context context;
    protected View itemView;
    protected LayoutInflater inflater;
    protected DialogSetting setting;
    @Nullable
    private AlertDialog dialog;

    protected String TAG = getClass().getSimpleName();

//    protected enum BtnEnum{
//        POSITIVE,
//        NEGATIVE,
//        NEUTRAL
//    }

//    protected enum DialogOption{
//        NoTitle,
//        CantCancelable,
//        NoDim,
//        Colorless,
//        FullWidth,
//        FullHeight,
//        FullScreen,
//        WrapContent,
//        ScreenBottom,
//        ScreenTop
//    }

    protected class DialogSetting {
        public DialogOption[] options;

        public DialogSetting(DialogOption... ops) {
            options = ops;
        }
    }

    public LibBaseAlertDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
        initView();
    }

    protected abstract DialogSetting setUpDialogFeature();

    private void init() {

        inflater = LayoutInflater.from(context);
        itemView = inflater.inflate(setInflatView(), null);
        setView(itemView);
        setting = setUpDialogFeature();
    }

    public void dismiss() {
        if (dialog != null)
            dialog.dismiss();
    }

    protected void creatDialogBtn(BtnEnum btnEnum, @StringRes int res) {
        creatDialogBtn(btnEnum, getString(res));
    }

    protected void creatDialogBtn(BtnEnum btnEnum, String btnName) {
        switch (btnEnum) {
            case POSITIVE:
                setPositiveButton(btnName, (DialogInterface dialog, int which) -> {
                    onPositiveClick(dialog, which);
                });
                break;
            case NEGATIVE:
                setNegativeButton(btnName, (DialogInterface dialog, int which) -> {
                    onNegativeClick(dialog, which);
                });
                break;
            case NEUTRAL:
                setNeutralButton(btnName, (DialogInterface dialog, int which) -> {
                    onNeutralClick(dialog, which);
                });
                break;
        }
    }

    protected void onPositiveClick(DialogInterface dialog, int which) {
    }

    protected void onNegativeClick(DialogInterface dialog, int which) {
    }

    protected void onNeutralClick(DialogInterface dialog, int which) {
    }

    protected String getString(@StringRes int res) {
        return context.getString(res);
    }

    protected int getColor(@ColorRes int res) {
        return context.getResources().getColor(res);
    }

    protected abstract APP getApp();

    protected abstract int getScreenHeight();

    protected abstract int getScreenWidth();
//    @Override
//    public AlertDialog show() {
//        return super.show();
//    }

    @Override
    public AlertDialog show() {
        dialog = create();
        Window window = dialog.getWindow();
        if (setting != null) {
            for (DialogOption op :
                    setting.options) {
                switch (op) {
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
                    case FullWidth:
                        window.setLayout(
                                getScreenWidth(),
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        break;
                    case FullHeight:
                        window.setLayout(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                getScreenHeight()
                        );
                        break;
                    case FullScreen:
                        window.setLayout(
                                getScreenWidth(),
                                getScreenHeight() * 95 / 100
                        );
                        break;
                    case WrapContent:
                        window.setLayout(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        break;
                }
            }
        }
        dialog.show();
        return dialog;
    }
}
