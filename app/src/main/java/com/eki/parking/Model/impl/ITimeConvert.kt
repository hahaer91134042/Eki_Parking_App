package com.eki.parking.Model.impl

import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/02/20
 */
interface ITimeConvert {
    fun startDateTime(from:DateTime=DateTime()):DateTime
    fun endDateTime(from:DateTime=DateTime()):DateTime
}