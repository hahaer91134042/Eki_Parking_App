package com.eki.parking.Controller.service

import android.app.job.JobService
import com.eki.parking.App

/**
 * Created by Hill on 2020/09/16
 */
abstract class BaseJobService:JobService() {

    protected var tag=this.javaClass.simpleName

    protected val app get()= App.getInstance()?:null
    protected val sqlManager=app?.sqlManager?:null
}