package com.hill.devlibs.asynctask;

import android.content.Context;
import android.os.AsyncTask;


import com.hill.devlibs.BaseApp;
import com.hill.devlibs.EnumClass.ProgressMode;
import com.hill.devlibs.dialog.LibProgressDialog;
import com.hill.devlibs.listener.OnPostExecuteListener;
import com.hill.devlibs.model.bean.ServerRequest;
import com.hill.devlibs.tools.Log;


import java.io.InputStream;

import androidx.collection.ArrayMap;

public abstract class LibBaseAsyncTask<Result,APP extends BaseApp,CALLBACK extends OnPostExecuteListener<Result> >extends AsyncTask<Void, Void, Result>{

	protected String result;

//	private LibProgressDialog progressDialog;
	protected ArrayMap<String, String> paramsMap = new ArrayMap<>();

	protected Context context;

	public boolean isDialog;

	public final String TAG=getClass().getSimpleName();

	public CALLBACK listener;

//	public interface onPostExecuteListener <R>{
//		 void onTaskPostExecute(@Nullable R o);
//	}

	public LibBaseAsyncTask(Context context, boolean isDialog){
		this.context=context;
		this.isDialog=isDialog;
	}


	@Override
	protected void onPostExecute(Result result) {
		closeProgress();
		if (listener!=null){
			listener.onTaskPostExecute(result);
		}
	}

	public LibBaseAsyncTask<Result, APP,CALLBACK> setExecuteListener(CALLBACK listener){
		this.listener=listener;
		return this;
	}

	protected abstract void showProgress(ProgressMode mode);
	protected abstract void closeProgress();

	protected String getString(int res) {
		return context.getString(res);
	}
	protected String[] getStringArr(int res){
		return context.getResources().getStringArray(res);
	}

	protected void printException(String which,Exception e){
		Log.e("-----"+which+"-----");
		printException(e);
	}
	protected void printException(Exception e){
		Log.e(TAG,e.toString());
	}
	public void start(){
		this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	protected abstract APP getApp();

}