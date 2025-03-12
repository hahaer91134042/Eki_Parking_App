package com.eki.parking.Controller.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by Hill on 2018/11/21.
 */
abstract class BaseService : Service() {
    protected var TAG=this::class.java.simpleName
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
    }
}