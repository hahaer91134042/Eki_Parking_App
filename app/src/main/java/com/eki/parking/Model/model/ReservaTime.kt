package com.eki.parking.Model.model

import com.eki.parking.AppConfig
import com.hill.devlibs.extension.toCalendar
import com.hill.devlibs.time.DateTime

data class ReservaTime(
    val EndTime: String,
    val IsUser: Boolean,
    val Remark: String,
    val StartTime: String
) {
    fun startDateTime() = DateTime(StartTime.toCalendar(AppConfig.ServerSet.dateTimeFormat))
    fun endDateTime() = DateTime(EndTime.toCalendar(AppConfig.ServerSet.dateTimeFormat))
}