package com.eki.parking.Controller.tools

import com.eki.parking.Model.DTO.OpenSet
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2020/05/13
 */
class OpenCheck(var openList:List<OpenSet>) {

    fun isOpen(time:DateTime):Boolean{
        return openList.any { it.startDateTime() <= time && time <= it.endDateTime() }
    }
}