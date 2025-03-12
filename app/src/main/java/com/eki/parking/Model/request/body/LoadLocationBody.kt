package com.eki.parking.Model.request.body

import com.eki.parking.AppConfig
import com.eki.parking.Controller.impl.IMultiPageConfig
import com.eki.parking.Model.DTO.LocationSearchTime
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.collection.SocketSelectList
import com.eki.parking.Model.request.abs.EkiRequestBody
import com.eki.parking.Model.sql.LoadLocationConfig
import com.hill.devlibs.impl.IConvertToSql
import com.hill.devlibs.time.DateTime

class LoadLocationBody: EkiRequestBody(),IMultiPageConfig{

    var lat:Double=0.0
    var lng:Double=0.0
    var address=RequestBody.Address()
    var config= Config()
    var nowTime=DateTime().toString() //不能刪

    override fun page(page: Int) {
        config.page=page
    }

    override fun requestApi(): EkiApi =EkiApi.LoadLocation

    class Config: IConvertToSql<LoadLocationConfig> {
        var range=AppConfig.MaxSearchRange.range
        var unit= AppConfig.MaxSearchRange.unit.toString()
        var page=1//不用紀錄
        var charges=ArrayList<Int>()
        var searchTime=LocationSearchTime()

        override fun toSql(): LoadLocationConfig =LoadLocationConfig().apply {
            Unit=unit
//            Page=page
            Range=range
            //這邊 沒有剛好等於全部的插頭類型都有
            if(charges.size>0){
                CarCharge= SocketSelectList(
                    SocketSelectList.car.filter {s-> charges.any {s.socket.value==it}})
                MotorCharge= SocketSelectList(
                    SocketSelectList.motor.filter {s-> charges.any {s.socket.value==it}})
            }

            SearchTime.also {
                it.date.clear()
                it.date.addAll(searchTime.date)
                if (searchTime.start.isNotEmpty())
                    it.start=searchTime.start
                if (searchTime.end.isNotEmpty())
                    it.end=searchTime.end
            }
        }
    }
}