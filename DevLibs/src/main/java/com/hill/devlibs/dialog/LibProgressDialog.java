package com.hill.devlibs.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.hill.devlibs.EnumClass.ProgressMode;
import com.hill.devlibs.R;


/**
 * Created by Hill on 2016/12/14.
 */

public class LibProgressDialog extends ProgressDialog {
    private Context context;
    private ProgressMode dialogMode;
//    public static final int USER_DEFIND_MODE=-1;
//    public static final int PROCESSING_MODE=0;
//    public static final int LOADING_MODE=1;
//    public static final int SIGN_IN_MODE=2;
//    public static final int CONNECT_MODE=3;

//    public enum Mode{
//        USER_DEFIND_MODE,
//        PROCESSING_MODE,
//        LOADING_MODE,
//        SIGN_IN_MODE,
//        CONNECT_MODE
//    }

   /**
    *@param context Activity context
   * @param mode progressDialog mode have two Title USER_DEFIND=自訂義 PROCESSING_MODE=處理中 LOADING_MODE=下載中
   *
   * */
    public LibProgressDialog(Context context, ProgressMode mode) {
        super(context);
        this.context=context;
        dialogMode=mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Log.d("Hill->", "ProgressDialogonCreate: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        setCancelable(false);
//        Log.d("Hill->", "ProgressDialogonStart: ");
        switch (dialogMode){
            case PROCESSING_MODE:
                setMessage(context.getString(R.string.Processing));
//                Log.d("Hill->", "ProgressDialogsetMessage: 22");
                break;
            case LOADING_MODE:
                setMessage(context.getString(R.string.Now_loading));
//                Log.d("Hill->", "ProgressDialogsetMessage: 22");
                break;
            case SIGN_IN_MODE:
                setMessage(context.getString(R.string.Sign_in));
                break;
            case CONNECT_MODE:
                setMessage(context.getString(R.string.Connecting));
                break;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
//        Log.d("Hill->", "ProgressDialogsetTitle: ");
    }

    @Override
    public void setMessage(CharSequence message) {

        super.setMessage(message);
//        Log.d("Hill->", "ProgressDialogsetMessage: 11");
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        }catch (Exception e){

        }
    }
}
