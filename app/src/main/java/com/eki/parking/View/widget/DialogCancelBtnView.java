package com.eki.parking.View.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.eki.parking.R;
import com.eki.parking.View.abs.LinearCustomView;

/**
 * Created by Hill on 2018/11/9.
 */
public class DialogCancelBtnView extends LinearCustomView {
    public Button cancelBtn;
    public ImageButton backBtn;
    public DialogCancelBtnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void backBtnShow(){
        backBtn.setVisibility(VISIBLE);
    }

    private void initView() {
        cancelBtn=itemView.findViewById(R.id.cancelBtn);
        backBtn=itemView.findViewById(R.id.backBtn);
    }

    @Override
    public ViewGroup.LayoutParams initNewLayoutParams() {
        return null;
    }

    @Override
    public int setInflatView() {
        return R.layout.dialog_cancel_btn_view;
    }
}
