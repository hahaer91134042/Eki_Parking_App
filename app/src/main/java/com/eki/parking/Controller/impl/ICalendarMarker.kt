package com.eki.parking.Controller.impl

import androidx.annotation.ColorInt
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/02/15
 */
abstract class ICalendarMarker {
    @get:ColorInt
    abstract val color:Int
    abstract val time:DateTime
    open var text=""
}