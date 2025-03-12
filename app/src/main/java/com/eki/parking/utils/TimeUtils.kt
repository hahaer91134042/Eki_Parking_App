package com.eki.parking.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    @JvmStatic
    fun stampToDateDash(time: Long, locale: Locale): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)

        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun stampToDateDefault(time: Long, locale: Locale): String {
        val simpleDateFormat = SimpleDateFormat("MM/dd HH:mm", locale)
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun dashDateToStamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", locale)

        return simpleDateFormat.parse(date).time
    }

    @JvmStatic
    fun dashDateTimeToStamp(datetime: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
        return simpleDateFormat.parse(datetime).time
    }
}