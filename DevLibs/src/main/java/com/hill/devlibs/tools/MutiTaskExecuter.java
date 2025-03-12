package com.hill.devlibs.tools;

import android.os.Handler;


import com.hill.devlibs.asynctask.LibBaseAsyncTask;
import com.hill.devlibs.listener.OnPostExecuteListener;
import com.hill.devlibs.model.ValueObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Hill on 2017/10/20.
 */

public class MutiTaskExecuter<VO extends ValueObject> implements Runnable ,
                                                                 OnPostExecuteListener<VO> {

    private LibBaseAsyncTask[] taskList;
    private ArrayList<VO> executeDataList=new ArrayList<>();
    private ExecuteOverListener listener;
    private Handler handler=new Handler();
    private int coolingTime=500,taskSequence=0,taskNum=0;

    @Override
    public void onTaskPostExecute(@NonNull VO o) {
        executeDataList.add(o);
    }


    public interface ExecuteOverListener<VO extends ValueObject>{
        void onExecuteOver(ArrayList<VO> dataList);
        void onLoadFail();
    }
    public MutiTaskExecuter(){}
    public MutiTaskExecuter(LibBaseAsyncTask... tasks){
        taskList=tasks;
        taskNum=taskList.length;
    }

    public MutiTaskExecuter setTasks(LibBaseAsyncTask... tasks){
        taskList=tasks;
        taskNum=taskList.length;
        return this;
    }

//    public void startWithDialog(Context context, int mode) {
//
//    }

    public void start() throws NullPointerException{
        if (taskList.length<1){
            throw new NullPointerException("Not any task for execute!");
        }else {
            for (LibBaseAsyncTask task:
                    taskList) {
                task.setExecuteListener(this).start();
            }
//            taskList[taskSequence].start();

            handler.post(this);
        }
    }

    public MutiTaskExecuter setExecuteOverListener(ExecuteOverListener listener){
        this.listener=listener;
        return this;
    }
    @Override
    public void run() {
        if (executeDataList.size()==taskList.length){
            taskList=null;
//            if (progressDialog!=null){
//                progressDialog.dismiss();
//                progressDialog=null;
//            }
            if (listener!=null){
                boolean isSuccess=true;
                for (VO data:
                     executeDataList) {
                    if (data==null){
                        isSuccess=false;
                        break;
                    }
                }
                if (isSuccess)
                    listener.onExecuteOver(executeDataList);
                else
                    listener.onLoadFail();
            }
        }else {
            handler.postDelayed(this,coolingTime);
        }
    }


}
