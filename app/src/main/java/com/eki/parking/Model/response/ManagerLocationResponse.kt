package com.eki.parking.Model.response

import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.sql.ManagerLocation

/**
 * Created by Hill on 2020/02/20
 */
class ManagerLocationResponse:ResponseVO(){
    var info=ArrayList<ManagerLocation>()

//    class InfoData:IConvertToSql<ManagerLocation>{
//        var Id=0
//        var Lat:Double=0.0
//        var Lng:Double=0.0
//        var Address = AddressInfo()
//        var Info = LocationInfo()
//        var Config = LocationConfig()
//        override fun toSql(): ManagerLocation =ManagerLocation().also {
//            it.Id=Id
//            it.Lat=Lat
//            it.Lng=Lng
//            it.Address=Address
//            it.Info=Info
//            it.Config=Config
//        }
//    }
}