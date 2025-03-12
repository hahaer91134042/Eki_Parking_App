package com.eki.parking.Model.sql

import com.eki.parking.Model.DTO.*
import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.impl.IDbQueryArgs
import com.eki.parking.Model.request.body.EditLocationBody
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IConvertData
import com.hill.devlibs.impl.IImgUrl
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.impl.ISocket
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.util.DbUtil

/**
 * Created by Hill on 2020/02/20
 */

@SqlTableSet(table = "ManagerLocation",isClear = false)
class ManagerLocation:SqlVO<ManagerLocation>(),
    IDbQueryArgs,
    IConvertData<EditLocationBody>,
    IImgUrl,
    ISocket{

    override fun queryBuilder(): IQueryArgsBuilder =object : IQueryArgsBuilder() {
        override val select: String
            get() = DbUtil.select("Id")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(Id.toString())
        override val where: String
            get() = DbUtil.where("Id",Id.toString())
    }

    @SqlColumnSet(key="Id",attr = SqlAttribute.INT,order = 1)
    var Id=0
    @SqlColumnSet(key="Lat",attr = SqlAttribute.DOUBLE,order = 2)
    var Lat:Double=0.0
    @SqlColumnSet(key="Lng",attr = SqlAttribute.DOUBLE,order = 3)
    var Lng:Double=0.0
    @SqlColumnSet(key = "Address",attr = SqlAttribute.Obj,order = 4)
    var Address: AddressInfo?=null
    @SqlColumnSet(key = "Info",attr = SqlAttribute.Obj,order = 5)
    var Info: LocationInfo?=null
    @SqlColumnSet(key = "Config",attr = SqlAttribute.Obj,order = 6)
    var Config: LocationConfig?=null
    @SqlColumnSet(key="RatingCount",attr = SqlAttribute.DOUBLE,order = 7)
    var RatingCount:Double=0.0
    //一開始的地點都是關閉的 所以預設可以刪除
    @SqlColumnSet(key="Deleteable",attr = SqlAttribute.BOOLEAN,order = 8)
    var Deleteable:Boolean=true
    @SqlColumnSet(key="Img",attr = SqlAttribute.Array,order = 9)
    var Img=ArrayList<LocationImg>()
    @SqlColumnSet(key = "Socket",attr = SqlAttribute.Array,order = 10)
    var Socket=ArrayList<LocationSocket>()

    @Deprecated("這Date欄位可能以後沒用 本來是要做多重月份時數計算 目前沒用")
    @SqlColumnSet(key = "Date",attr = SqlAttribute.TEXT,order = 11)
    var Date:String=""

    //基本上 地點使用的電流型態只有一種 但插頭總類可以多個
    val current: CurrentEnum
        get() = Socket.groupBy { it.currentEnum }
            .keys.firstOrNull() ?: CurrentEnum.NONE

    val sockets: List<ChargeSocket>
        get() = when {
            Socket.size < 1 -> listOf(ChargeSocket.NONE)
            else -> Socket.map { it.chargeSocket }
        }

    var time:DateTime
        get() = DateTime.parse(Date)
        set(value) {Date=value.toString()}

    override fun clear() {}

    override fun copyFrom(from: ManagerLocation): Boolean =
        runCatching {
            Id=from.Id
            Lat=from.Lat
            Lng=from.Lng
            from.Address.notNull { Address= AddressInfo().apply { copyFrom(it) } }
            from.Info.notNull { Info= LocationInfo().apply { copyFrom(it) } }
            from.Config.notNull { Config= LocationConfig().apply { copyFrom(it) } }
            Img=from.Img
            Socket=from.Socket
            Date=from.Date
        }.isSuccess

    override fun toData(): EditLocationBody =
        EditLocationBody().also {loc->
            loc.id=Id
            Info.notNull { loc.info=it.toData() }
            Config.notNull { loc.config = it.toData() }
            Socket.forEach { socket ->
                loc.socket.addAll(arrayListOf(socket.toData()))
            }
        }

    override fun imgUrl(): String = Img.minByOrNull { it.Sort }?.Url?:""
    override fun currentValue(): Int = Socket.first().Current
    override fun chargeValue(): Int = Socket.first().Charge
}