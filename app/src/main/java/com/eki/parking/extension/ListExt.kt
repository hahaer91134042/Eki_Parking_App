package com.eki.parking.extension

import com.eki.parking.Model.DTO.ReservaSet
import com.eki.parking.Model.sql.EkiLocation
import com.hill.devlibs.extension.toArrayList

/**
 * Created by Hill on 29,10,2019
 */
fun ArrayList<EkiLocation>.select(lat:Double,lng:Double):ArrayList<EkiLocation>{
    var newList=ArrayList<EkiLocation>()
    newList.addAll(this.filter { it.Lat==lat&&it.Lng==lng })
    return newList
}

fun List<ReservaSet>.mapToReservaDelay():ArrayList<ReservaSet>{
    return this.map {r->
        var end=r.endDateTime()//+AppConfig.ReservaDelayMin.minSpan //Linda:調整讓畫面和Ios顯示一樣(預約 00:00 - 00:30 時車位預定條顯示兩格
        ReservaSet().apply {
            StartTime=r.StartTime
            EndTime=end.toString()
            Remark=r.Remark
            IsUser=r.IsUser
        }
    }.toArrayList()
}



