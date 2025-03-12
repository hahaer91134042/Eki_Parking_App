package com.eki.parking.Controller.calendar.marker

import com.eki.parking.Controller.impl.ICalendarMarker
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/02/15
 */
class SimpleMarker(override val time: DateTime,
                   override val color: Int):ICalendarMarker(){

}