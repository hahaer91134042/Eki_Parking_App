package com.eki.parking.Controller.service

import android.app.job.JobParameters
import android.os.Handler
import com.hill.devlibs.tools.Log


/**
 * Created by Hill on 2020/09/16
 */
@Deprecated("目前沒用")
class AlarmReservaTimeJob :BaseJobService(){

    private var count=0
    private var handler=Handler()

    override fun onStartJob(params: JobParameters?): Boolean {



        Log.w("$tag  onStartJob params->$params")

        handler.post(runable)

        return true
    }

    private var runable= object:Runnable {
        override fun run() {
            Log.i("$tag run count->$count")
            count++

            handler.postDelayed(this,1000)
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {

        Log.i("$tag onStopJob ")
        return false
    }
}