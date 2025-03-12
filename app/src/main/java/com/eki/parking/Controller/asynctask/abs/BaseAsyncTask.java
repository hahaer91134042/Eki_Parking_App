package com.eki.parking.Controller.asynctask.abs;


import com.eki.parking.App;

import com.eki.parking.Controller.dialog.EkiProgressDialog;
import com.hill.devlibs.EnumClass.ProgressMode;
import com.hill.devlibs.asynctask.LibBaseAsyncTask;
import com.hill.devlibs.listener.OnPostExecuteListener;


import android.content.Context;

public abstract class BaseAsyncTask<Result,CALLBACK extends OnPostExecuteListener<Result>> extends LibBaseAsyncTask<Result,App,CALLBACK> {

    private EkiProgressDialog progressDialog;

	public BaseAsyncTask(Context context,boolean isDialog){
	    super(context,isDialog);
	}

    @Override
    protected void showProgress(ProgressMode mode) {
	    try {
            if (isDialog) {
                if (progressDialog==null)
                    progressDialog = new EkiProgressDialog(context, mode);
                progressDialog.show();
            }
        }catch (Exception e){
	        progressDialog=null;
        }
    }

    @Override
    protected void closeProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            //progressDialog = null;
        }
    }

    @Override
    protected App getApp() {
        return App.getInstance();
    }
}