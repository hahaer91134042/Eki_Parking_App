package com.eki.parking.Controller.impl

import android.content.Intent
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/09/18
 */
interface ISysAlarmSet {
    fun alarmTime():DateTime
    fun intent():Intent
}