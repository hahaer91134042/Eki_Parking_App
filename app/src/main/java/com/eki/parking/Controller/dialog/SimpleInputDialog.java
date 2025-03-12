package com.eki.parking.Controller.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.eki.parking.R;
import com.eki.parking.Controller.util.ScreenUtils;
import com.hill.devlibs.tools.Log;

import java.util.ArrayList;

/**
 * Created by Hill on 2017/9/29.
 */

public class SimpleInputDialog <L extends SimpleInputDialog.ButtonClicKListener> extends AlertDialog.Builder {

    private Context context;
    private int width;
    private TextView msgTexView;
    private EditText inputEditTex;
    private ButtonClicKListener btnClickListener;
    private ListClickListener listClickListener;
    private String[] item;


    public interface ListClickListener extends ButtonClicKListener{
        void onItemClick(int position);
    }

    public interface ButtonClicKListener{
        void onCancelBtn();
        void onDeterminBtn(String inputStr);
    }
    public SimpleInputDialog(Context context) {
        super(context);
        this.context=context;
        this.width=getWidth();
        creatInputView();
        setCancelable(false);

        setRightBtnText(context.getString(R.string.Determine));
        setLeftBtnText(context.getString(R.string.Cancel));
    }
    public SimpleInputDialog(Context context, ArrayList<String> itemList){
        super(context);
        this.context=context;

        setCancelable(false);

        item=new String[itemList.size()];
        for (int i = 0; i <itemList.size() ; i++) {
            item[i]=itemList.get(i);
        }

        setSingleChoiceItems(item,-1,btnListener);

        setRightBtnText(context.getString(R.string.Determine));
        setLeftBtnText(context.getString(R.string.Cancel));

    }

    public enum TextInputType{
        CLASS_TEXT(InputType.TYPE_CLASS_TEXT),
        NUMBRE(InputType.TYPE_CLASS_NUMBER),
        MAIL(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS),
        PASSWORD(InputType.TYPE_TEXT_VARIATION_PASSWORD)
        ;

        public int inputType;
        TextInputType(int t) {
            inputType=t;
        }
    }
    private void creatInputView() {
        LinearLayout container=new LinearLayout(context);
        container.setLayoutParams(new LinearLayout.LayoutParams(
                width*60/100,
                width
        ));
        container.setOrientation(LinearLayout.VERTICAL);
        msgTexView=new TextView(context);
        msgTexView.setTextColor(Color.BLACK);
        msgTexView.setTextSize(20f);
        msgTexView.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL|Gravity.CENTER);
        container.addView(msgTexView);
        inputEditTex=new EditText(context);
        inputEditTex.setInputType(TextInputType.CLASS_TEXT.inputType);
        inputEditTex.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL|Gravity.CENTER);
        inputEditTex.setTextSize(20f);
        container.addView(inputEditTex);
        setView(container);
    }

    public SimpleInputDialog setInputType(TextInputType textEnum){
        inputEditTex.setInputType(textEnum.inputType);
        return this;
    }

    public SimpleInputDialog setMsgText(String msg){
        msgTexView.setText(msg);
        return this;
    }
    public SimpleInputDialog setEditText(String inputTex){
        inputEditTex.setText(inputTex);
        return this;
    }
    public SimpleInputDialog setEditTextHint(String hintTex){
        inputEditTex.setHint(hintTex);
        return this;
    }
    public SimpleInputDialog setDialogListener(L listener){
        if (listener instanceof ListClickListener){
            listClickListener=(ListClickListener) listener;
        }else {
            btnClickListener=listener;
        }
        return this;
    }

    private void setLeftBtnText(String tex){
        setNegativeButton(tex,btnListener);//-2
    }

    private void setRightBtnText(String tex){
        setPositiveButton(tex,btnListener);//-1
    }

    private int itemPosition;
    private DialogInterface.OnClickListener btnListener= new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.e(getClass().getSimpleName(),"onClick->"+which);
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(inputEditTex.getWindowToken(), 0);

            switch (which){
                case DialogInterface.BUTTON_NEGATIVE:
                    if (btnClickListener!=null){
                        btnClickListener.onCancelBtn();
                    }else if (listClickListener!=null){
                        listClickListener.onCancelBtn();
                    }
                    break;
                case DialogInterface.BUTTON_POSITIVE:
                    if (btnClickListener!=null){
                        btnClickListener.onDeterminBtn(inputEditTex.getText().toString().trim());
                    }else if (listClickListener!=null){
                        listClickListener.onDeterminBtn(item[itemPosition]);
                    }
                    break;
                default:
                    if (which!=DialogInterface.BUTTON_NEUTRAL){
                        itemPosition=which;
                        if (listClickListener!=null){
                            listClickListener.onItemClick(which);
                        }
                    }

                    break;
            }
        }
    };

    private int getWidth(){
        return ScreenUtils.getScreenWidth();
    }

}
