package com.eki.parking.Model.DTO

import com.eki.parking.Model.impl.ITimeConvert
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit
import com.hill.devlibs.time.TimeUnit

/**
 * Created by Hill on 2021/11/23
 */
open class TimeRange(val start:DateTime,val end:DateTime):ITimeConvert {

    constructor(start:DateUnit,end:DateUnit):this(DateTime(start,TimeUnit(0,0,0)), DateTime(end,TimeUnit(24,0,0)))

    override fun startDateTime(from: DateTime): DateTime =start

    override fun endDateTime(from: DateTime): DateTime =end

    //判斷是否重疊
    fun isOverLap(other:TimeRange):Boolean{
        //都小於
        if(start<=other.start&&end<=other.start)
            return false
        //都大於
        if(start>=other.end&&end>=other.end)
            return false

        return true
    }
}