package com.hill.devlibs.asynctask

import android.os.AsyncTask

/**
 * Created by Hill on 2020/02/12
 */
abstract class SimpleAsyncTask<Result>: AsyncTask<Void, Void, Result>(){

    override fun doInBackground(vararg params: Void?): Result {
        return doInBackground()
    }
    override fun onPostExecute(result: Result) {
        runOnUI(result)
    }


    abstract fun doInBackground():Result
    abstract fun runOnUI(result: Result)

    fun start() {
        executeOnExecutor(THREAD_POOL_EXECUTOR)
    }
}