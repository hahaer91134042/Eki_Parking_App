package com.eki.parking.View.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.eki.parking.R;
import com.eki.parking.View.abs.UnderLineLayout;

/**
 * Created by Hill on 2017/11/14.
 */

public class AccountInputView extends UnderLineLayout {

    private ImageView headerIcon, tailIcon;
    private EditText accInputTex;
    private int iconWidth,texColor=Color.BLACK;
    private String acc="";

    public AccountInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public LayoutParams initNewLayoutParams() {
        return null;
    }

    @Override
    public int setInflatView() {
        return R.layout.item_inputview;
    }

    public void initView() {


        headerIcon = (ImageView) itemView.findViewById(R.id.headerIcon);
        headerIcon.getLayoutParams().width = iconWidth;
        headerIcon.getLayoutParams().height = iconWidth;
        headerIcon.setImageResource(R.drawable.mail);
        tailIcon = (ImageView) itemView.findViewById(R.id.tailIcon);
        tailIcon.setVisibility(INVISIBLE);

        accInputTex = (EditText) itemView.findViewById(R.id.inputTex);
//        accInputTex.setFocusableInTouchMode(false);
        accInputTex.setOnClickListener((v) -> {
//            PrintLogKt.i("accInputTex onClick");
            setLineColor(Color.BLACK);
        });
        accInputTex.setOnFocusChangeListener((View v, boolean hasFocus) -> {
//            PrintLogKt.d("acc input hasFocus->" + hasFocus);
            if (hasFocus) {
                setLineColor(Color.BLACK);
            }
        });
        accInputTex.setText(acc);
        accInputTex.setTextColor(texColor);
        accInputTex.setTextSize(15f);
        accInputTex.setGravity(Gravity.CENTER);
        accInputTex.setHint(context.getString(R.string.Your_email));
        accInputTex.setHintTextColor(texColor);
        accInputTex.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    }

    public String getInputAcc() {
        disableFocuse();
        return accInputTex.getText().toString().trim();
    }

    public void disableFocuse() {
        accInputTex.clearFocus();
    }

    public AccountInputView setIconWidth(int width) {
        iconWidth = width;
        return this;
    }
    public AccountInputView setTextColor(int color){
        texColor=color;
        return this;
    }

    public AccountInputView setAcc(String acc) {
        this.acc=acc;
        return this;
    }
}
