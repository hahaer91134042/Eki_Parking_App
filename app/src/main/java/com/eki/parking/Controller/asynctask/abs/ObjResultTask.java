package com.eki.parking.Controller.asynctask.abs;

import android.content.Context;

//import com.eki.parking.model.response.EkiResponse;
import com.hill.devlibs.listener.OnPostExecuteListener;
import com.hill.devlibs.model.ValueObject;

/**
 * Created by Hill on 2018/3/2.
 */

public abstract class ObjResultTask<VO extends ValueObject,
                                    CALLBACK extends OnPostExecuteListener<VO>>
                                    extends BaseAsyncTask<VO,CALLBACK> {

    //protected static String[] errorMsg;
//    protected EkiApi apiEnum;
//    protected EkiRequest request;

    public ObjResultTask(Context context, boolean isDialog) {
        super(context, isDialog);
        //errorMsg = context.getResources().getStringArray(R.array.responseMsg);
    }

    @Override
    protected void onPostExecute(VO result) {
        super.onPostExecute(result);
    }

//    protected  EkiResponse getLoadErrorSerponse(String msg) {
//        return (EkiResponse) new EkiResponse();
//    }



}
