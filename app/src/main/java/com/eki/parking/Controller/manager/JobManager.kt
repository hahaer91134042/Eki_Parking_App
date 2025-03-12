package com.eki.parking.Controller.manager

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.eki.parking.Controller.service.EkiSocketJob
import com.eki.parking.Controller.tools.ForegroundCallbacks
import com.hill.devlibs.tools.Log


/**
 * Created by Hill on 2020/09/16
 */
class JobManager(context: Context?) :BaseManager(context) {

    interface JobConfig{
        val jobId:Int
        val jobClazz:Class<*>
        //隨者app取消
//        val destroyByApp:Boolean
        val runInBackground:Boolean
        fun setUpJobInfo(info:JobInfo.Builder):JobInfo.Builder
    }

    private val jobList= listOf<JobConfig>(
            EkiSocketJob()
    )

    private var isFirst=true

    init {
        ForegroundCallbacks.getInstance().addListener(object : ForegroundCallbacks.Listener{
            override fun onBecameForeground() {
                Log.i("$TAG app foreground")
                if (isFirst){
                    isFirst=false
                    start()
                }else{
                    start(true)
                }
            }

            override fun onBecameBackground() {
                Log.w("$TAG app background")
                stop(true)
            }
        })
    }

    private fun start(checkBack:Boolean=false){

        var scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        jobList.forEach { config->


            var componentName = ComponentName(context,config.jobClazz)
            val job = config.setUpJobInfo(JobInfo.Builder(config.jobId, componentName))
                    .build()
//                    .setPersisted(true) // 重開機後是否執行
//                    .setMinimumLatency(2000) // 延遲多久執行

                    //.setOverrideDeadline(20000)  //最晚延遲時間，若其他條件未滿足，一樣會執行
                    //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) //網路條件
                    // .setRequiresCharging(true) //在充電情況下才執行
//                    .build()

            //调用schedule
            if (!checkBack){
                scheduler.schedule(job)
            }else{
                if (!config.runInBackground)
                    scheduler.schedule(job)
                else{
                    var runJob=scheduler.allPendingJobs.firstOrNull{info->info.id==config.jobId}
//                    Log.i("$TAG runJob->$runJob")
                    //假如關閉了 再開啟
                    if (runJob==null)
                        scheduler.schedule(job)
                }
            }
        }

    }

    //似乎沒什麼用
    private fun stop(isBack:Boolean=false){
        var scheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        jobList.forEach {config->

            if (isBack){
                if (!config.runInBackground)
                    scheduler.cancel(config.jobId)
            }else{
                scheduler.cancel(config.jobId)
            }
        }
    }

}