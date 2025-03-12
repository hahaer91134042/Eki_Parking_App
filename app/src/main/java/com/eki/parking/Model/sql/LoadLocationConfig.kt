package com.eki.parking.Model.sql

import android.content.ContentValues
import com.eki.parking.AppConfig
import com.hill.devlibs.EnumClass.DistanceUnit
import com.eki.parking.Model.collection.SocketSelectList
import com.hill.devlibs.impl.IConvertData
import com.eki.parking.Model.DTO.LocationSearchTime
import com.eki.parking.Model.DTO.SocketSelect
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.EnumClass.SiteSize
import com.eki.parking.Model.request.body.LoadLocationBody
import com.eki.parking.extension.toEnum
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.collection.DataRow
import com.hill.devlibs.extension.printContent
import com.hill.devlibs.extension.printValue
import com.hill.devlibs.extension.setObjData
import com.hill.devlibs.extension.toContentValue
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 16,10,2019
 */
@SqlTableSet(table = "LoadLocationConfig")
class LoadLocationConfig : SqlVO<LoadLocationConfig>(),
                           IConvertData<LoadLocationBody.Config> {

    @SqlColumnSet(key = "Id", attr = SqlAttribute.ID, order = 1)
    var Id = 0
    @SqlColumnSet(key = "Unit", attr = SqlAttribute.TEXT, order = 2)
    var Unit = AppConfig.MaxSearchRange.unit.toString()
    @SqlColumnSet(key = "Range", attr = SqlAttribute.INT, order = 3)
    var Range = AppConfig.MaxSearchRange.range
    @SqlColumnSet(key = "CarCharge", attr = SqlAttribute.Array, order = 4)
    var CarCharge = SocketSelectList(SocketSelectList.car)//汽車的插頭搜尋
    @SqlColumnSet(key = "MotorCharge", attr = SqlAttribute.Array, order = 5)
    var MotorCharge = SocketSelectList(SocketSelectList.motor)//機車的插頭搜尋
    @SqlColumnSet(key = "SearchTime", attr = SqlAttribute.Obj, order = 6)
    var SearchTime = LocationSearchTime()
    @SqlColumnSet(key = "Address", attr = SqlAttribute.TEXT, order =7)
    var Address=""
    @SqlColumnSet(key="Vehicle", attr = SqlAttribute.TEXT, order = 8)
    var Vehicle=SearchVehicle.CarAndMotor.toString()





    fun isDefault():Boolean{
        var default=LoadLocationConfig()
//        Log.w("config ori Unit->$Unit default->${default.Unit} bool->${Unit==default.Unit}")
//        Log.w("config ori Range->$Range default->${default.Range} bool->${Range==default.Range}")
//        Log.w("config ori CarCharge->$CarCharge default->${default.CarCharge} bool->${CarCharge==default.CarCharge}")
//        Log.w("config ori MotorCharge->$MotorCharge default->${default.MotorCharge} bool->${MotorCharge==default.MotorCharge}")
//        Log.w("config ori SearchTime bool->${SearchTime.isDefault()}")
//        Log.w("config ori MotorCharge->$MotorCharge default->${default.MotorCharge} bool->${MotorCharge==default.MotorCharge}")

        return Unit==default.Unit&&
                Range==default.Range&&
                CarCharge==default.CarCharge&&
                MotorCharge==default.MotorCharge&&
                SearchTime.isDefault()&&
//                Address==default.Address&&  //以後地址需要再修改
                Vehicle==default.Vehicle
    }

    var unitEnum
        get() = Unit.toEnum<DistanceUnit>()
        set(value) {
            Unit = value.toString()
        }
    var searchVehicle
        get() = Vehicle.toEnum<SearchVehicle>()
        set(value) {
            Vehicle = value.toString()
        }

    /**
    * 之後所有的socket全部都下載 在本機篩選
     * charges是空陣列 表示全部下載
    ***/
    override fun toData(): LoadLocationBody.Config = LoadLocationBody.Config().apply {
//        unit = Unit
//        page = Page
        range = Range
        unit = Unit
//        Charges.forEach {
            //之後所有的socket全部都下載 在本機篩選
//            it.socket?.value.notNull { v->charges.add(v) }
//        }

        //這邊要轉換 假如日期沒有設定 時間就全部變成空字串
        searchTime=LocationSearchTime().also {
            it.date.addAll(SearchTime.date)
            when{
                SearchTime.date.size<1->{
                    it.start=""
                    it.end=""
                }
                else->{
                    it.start=SearchTime.start
                    it.end=SearchTime.end
                }
            }
        }

    }

    fun checkVehicle(loc:EkiLocation):Boolean{

        return searchVehicle.content.any { it==loc.Info?.siteSize }
    }

    fun checkSocket(loc: EkiLocation):Boolean{
        return when{
            //假如都沒有插頭篩選條件 就只以車型作判別
            CarCharge.size<1&&MotorCharge.size<1->searchVehicle.content.any { it==loc.Info?.siteSize }
            else->{
//                var vehicle=ArrayList<SiteSize>()
//                if(CarCharge.size>0)
//                    vehicle.addAll(SearchVehicle.Car.content)
//                if (MotorCharge.size>0)
//                    vehicle.addAll(SearchVehicle.Motor.content)

                var locSocketList=SocketSelectList(when{
                    loc.Socket.size<1-> listOf(SocketSelect.None)
                    else->loc.Socket.map { SocketSelect(it.currentEnum,it.chargeSocket) }
                })

                if(loc.Info?.SerialNumber=="RTI-526710758"){
                    Log.i("RTI-526710758 locSocketList->$locSocketList")
                    Log.d("MotorCharge->$MotorCharge")
                    Log.w("CarCharge->$CarCharge")
                }

                when(loc.Info?.siteSize){
                    SiteSize.Motor->MotorCharge.any {s->locSocketList.any { s==it } }
                    SiteSize.Small->CarCharge.any {s->locSocketList.any { s==it } }
                    SiteSize.Standar->CarCharge.any {s->locSocketList.any { s==it } }
                    else->false
                }
            }
        }
    }


    override fun contentValues(): ContentValues {
        return toContentValue()
    }

    override fun initFromRow(row: DataRow): Boolean {
        return row.setObjData(this)
    }

    override fun copyFrom(from: LoadLocationConfig): Boolean
    = runCatching {
        Id=from.Id
        Unit=from.Unit
        Range=from.Range
        CarCharge=from.CarCharge
        MotorCharge=from.MotorCharge
        SearchTime=from.SearchTime
        Address=from.Address
        Vehicle=from.Vehicle
    }.isSuccess

    override fun clear() {

    }

    override fun printValue() {
        printObj(this)
        printContent(SearchTime)
    }

    enum class SearchVehicle(var content:List<SiteSize>){
        CarAndMotor(listOf(SiteSize.Motor,SiteSize.Standar,SiteSize.Small)),
        Car(listOf(SiteSize.Standar,SiteSize.Small)),
        Motor(listOf(SiteSize.Motor))
    }
}