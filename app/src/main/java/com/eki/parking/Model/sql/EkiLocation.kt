package com.eki.parking.Model.sql

import android.content.ContentValues
import android.content.Context
import com.eki.parking.Controller.tools.FindMarker
import com.eki.parking.Model.DTO.*
import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.impl.ICurrencyCost
import com.eki.parking.Model.EnumClass.CurrencyUnit
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.R
import com.eki.parking.Model.impl.IDbQueryArgs
import com.eki.parking.Model.impl.IMarkerFeature
import com.eki.parking.Model.EnumClass.LocAvailable
import com.eki.parking.View.widget.LocationMarkerView
import com.eki.parking.extension.*
import com.google.android.gms.maps.model.LatLng
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.collection.DataRow
import com.hill.devlibs.extension.*
import com.hill.devlibs.impl.IImgUrl
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.impl.ISocket
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.util.DbUtil
import kotlin.collections.ArrayList

@SqlTableSet(table = "EkiLocation")
class EkiLocation:SqlVO<EkiLocation>(),
        IMarkerFeature<LocationMarkerView>,
        IDbQueryArgs,
        IImgUrl,ICurrencyCost,
        ISocket {

    override fun queryBuilder(): IQueryArgsBuilder =object : IQueryArgsBuilder() {
        override val select: String
            get() = DbUtil.select("Id")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(Id.toString())
        override val where: String
            get() = DbUtil.where("Id",Id.toString())
    }
    //這邊是伺服器的ID
    @SqlColumnSet(key="Id",attr = SqlAttribute.INT,order = 1)
    var Id=0
    @SqlColumnSet(key="Lat",attr = SqlAttribute.DOUBLE,order = 2)
    var Lat:Double=0.0
    @SqlColumnSet(key="Lng",attr = SqlAttribute.DOUBLE,order = 3)
    var Lng:Double=0.0
    @SqlColumnSet(key="Available",attr = SqlAttribute.INT,order = 4)
    var Available=0
    @SqlColumnSet(key = "RatingCount",attr = SqlAttribute.DOUBLE,order = 5)
    var RatingCount=0.0
    @SqlColumnSet(key = "Address",attr = SqlAttribute.Obj,order = 7)
    var Address: AddressInfo?=null
    @SqlColumnSet(key = "Info",attr = SqlAttribute.Obj,order = 8)
    var Info: LocationInfo?=null
    @SqlColumnSet(key = "Config",attr = SqlAttribute.Obj,order = 9)
    var Config: LocationConfig?=null
    @SqlColumnSet(key = "Img",attr = SqlAttribute.Array,order = 10)
    var Img=ArrayList<LocationImg>()
    @SqlColumnSet(key = "Socket",attr = SqlAttribute.Array,order = 11)
    var Socket=ArrayList<LocationSocket>()

    var available:LocAvailable
        get() = Available.toEnum()
        set(value) {Available=value.value}

    //基本上 地點使用的電流型態只有一種 但插頭總類可以多個
    val current: CurrentEnum
        get() = Socket.groupBy { it.currentEnum }
            .keys.firstOrNull() ?: CurrentEnum.NONE

    val sockets: List<ChargeSocket>
        get() = when {
            Socket.size < 1 -> listOf(ChargeSocket.NONE)
            else -> Socket.map { it.chargeSocket }
        }
    

//    override fun queryBuilder(): DbUtil.QueryArgs =DbUtil.getQueryArgs().apply {
//        select=DbUtil.select("Id")
//        selectArgs=DbUtil.selectArgs(Id.toString())
//        where=DbUtil.where("Id",Id.toString())
//    }

    override fun contentValues(): ContentValues {
        return toContentValue()
    }

    override fun initFromRow(row: DataRow): Boolean {
//        var ok=runCatching {
//            TableParser.setData(this@EkiLocation,row)
//        }.isSuccess
//        Log.w("$TAG initFromRow ok->$ok")
        return row.setObjData(this)
    }

    override fun clear() {}

    override fun printValue() {
        printObj(this)
        Address?.printValue()
        Info?.printValue()
        Config?.printValue()
        Socket.printValue()
    }

    override fun getPosition(): LatLng = LatLng(Lat,Lng)
    override fun getMarkerTitle(): String =""
    override fun getSnippet(): String =Info?.SerialNumber?:Id.toString()
    override fun getLocationData(): EkiLocation =this

    override fun getMarkerView(context: Context?): LocationMarkerView {
        return LocationMarkerView(context).apply {

            setImg(FindMarker.fromLoc(this@EkiLocation).res)

            setAvailable(available)

            when(Config?.currencyUnit){
                CurrencyUnit.USD->setText(string(R.string.Price_format).messageFormat(Config?.Price.toString()))
                else->setText(string(R.string.Price_format).messageFormat(Config?.Price?.toInt().toString()))
            }
        }
    }

    override fun imgUrl(): String = Img.minByOrNull { it.Sort }?.Url?:""
    override val costValue: Double
        get() = Config?.Price?:0.0
    override val currencyUnit: Int
        get() = Config?.Unit?:0
    override fun currentValue(): Int = Socket.first().Current
    override fun chargeValue(): Int = Socket.first().Charge

}