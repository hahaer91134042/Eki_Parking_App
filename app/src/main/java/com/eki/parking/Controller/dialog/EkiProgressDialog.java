package com.eki.parking.Controller.dialog;

import android.content.Context;

import com.hill.devlibs.EnumClass.ProgressMode;
import com.hill.devlibs.dialog.LibProgressDialog;


/**
 * Created by Hill on 2016/12/14.
 */

public class EkiProgressDialog extends LibProgressDialog {

    /**
     * @param context Activity context
     * @param mode    progressDialog mode have two Title USER_DEFIND=自訂義 PROCESSING_MODE=處理中 LOADING_MODE=下載中
     */
    public EkiProgressDialog(Context context, ProgressMode mode) {
        super(context, mode);
    }
}
