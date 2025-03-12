package com.eki.parking.Model.sql

import com.eki.parking.AppConfig
import com.eki.parking.Model.DTO.*
import com.eki.parking.Model.EnumClass.OrderStatus
import com.eki.parking.Model.impl.ICurrencyCost
import com.eki.parking.Model.impl.IDbQueryArgs
import com.eki.parking.Model.impl.ITimeConvert
import com.eki.parking.extension.parseEnum
import com.eki.parking.extension.standarByTimeOffset
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.util.DbUtil
import java.util.*

/**
 * Created by Hill on 13,11,2019
 */

@SqlTableSet(table = "EkiOrder")
class EkiOrder :SqlVO<EkiOrder>(), IDbQueryArgs, ICurrencyCost,ITimeConvert {

    override fun queryBuilder(): IQueryArgsBuilder =object :IQueryArgsBuilder(){
        override val select: String
            get() = DbUtil.select("SerialNumber")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(SerialNumber)
        override val where: String
            get() = DbUtil.where("SerialNumber",SerialNumber)
    }

    @SqlColumnSet(key="Id",attr = SqlAttribute.ID,order = 1)
    var Id=0
    @SqlColumnSet(key="SerialNumber",attr = SqlAttribute.TEXT,order = 2)
    var SerialNumber:String=""
    @SqlColumnSet(key="Cost",attr = SqlAttribute.DOUBLE,order = 3)
    var Cost:Double=0.0
    @SqlColumnSet(key="Unit",attr = SqlAttribute.INT,order = 4)
    var Unit:Int=0
    @SqlColumnSet(key="HandlingFee",attr = SqlAttribute.DOUBLE,order = 5)
    var HandlingFee:Double=0.0
    @SqlColumnSet(key = "Status",attr = SqlAttribute.INT,order = 6)
    var Status:Int=0
    @SqlColumnSet(key = "CarNum",attr = SqlAttribute.TEXT,order = 7)
    var CarNum:String=""
    @SqlColumnSet(key = "CreatTime",attr = SqlAttribute.TEXT,order = 8)
    var CreatTime=""
    @SqlColumnSet(key = "Rating",attr = SqlAttribute.BOOLEAN,order = 9)
    var Rating=false
    @SqlColumnSet(key = "Argue",attr = SqlAttribute.BOOLEAN,order = 10)
    var Argue=false
    @SqlColumnSet(key = "CheckOutUrl",attr = SqlAttribute.TEXT,order = 11)
    var CheckOutUrl=""
    @SqlColumnSet(key = "Cp",attr = SqlAttribute.Obj,order = 12)
    var Cp: Cp? = Cp()
    @SqlColumnSet(key = "ReservaTime",attr = SqlAttribute.Obj,order = 13)
    var ReservaTime= ReservaSet()
    @SqlColumnSet(key = "Address",attr = SqlAttribute.Obj,order = 14)
    var Address= AddressInfo()
    @SqlColumnSet(key = "Checkout",attr = SqlAttribute.Obj,order = 15)
    var Checkout:OrderCheckout?=null
    @SqlColumnSet(key = "LocPrice",attr = SqlAttribute.DOUBLE,order = 16)
    var LocPrice:Double=0.0
    @SqlColumnSet(key = "LocSerial",attr = SqlAttribute.TEXT,order = 17)
    var LocSerial=""
    @SqlColumnSet(key = "Member",attr = SqlAttribute.Obj,order = 18)
    var Member:ResponseInfo.OrderMember?=null //這物件只有Manager LocOrder 下載來的才有
    @SqlColumnSet(key = "Invoice",attr = SqlAttribute.Obj,order = 19)
    var Invoice:OrderInvoice?=null

    var orderStatus: OrderStatus
        set(value) { Status=value.value }
        get() = parseEnum(Status)
    val creatTime: DateTime
        get() = CreatTime.toDateTime(AppConfig.ServerSet.dateTimeFormat)


    override fun clear() {}

    fun isReserved():Boolean{
        return when(orderStatus){
            OrderStatus.Reserved,
            OrderStatus.InUsing->DateTime.now()< ReservaTime.endDateTime()
            else -> false
        }
    }

    fun isPrepared():Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(ReservaTime.startDateTime().year,
            ReservaTime.startDateTime().month-1,
            ReservaTime.startDateTime().day,
            ReservaTime.startDateTime().hour,
            ReservaTime.startDateTime().min.minus(5))
        return when(orderStatus) {
            OrderStatus.Reserved, OrderStatus.InUsing-> {
                DateTime(calendar) < DateTime.now()
                        && DateTime.now() < ReservaTime.endDateTime()
            }
            else -> false
        }
    }

    //篩選出待付款 已結清 或者是取消之後產生的費用 不用再走checkout 流程
    fun isBeSettle():Boolean{
        return when(orderStatus){
            OrderStatus.PayError,
            OrderStatus.BeSettle->true
            else -> CheckOutUrl.isNotEmpty()
        }
    }

    //這是找出已經使用完畢 待結清的訂單 要走結清流程
    fun isOverCheckOut():Boolean{
        return when(orderStatus){
            OrderStatus.Reserved,
            OrderStatus.InUsing->ReservaTime.endDateTime()<=DateTime.now()
            else -> CheckOutUrl.isNotEmpty()
        }
    }

    //這是找出已經可以進行結帳的訂單
    fun isBeCheckOut():Boolean{
        return when(orderStatus){
            OrderStatus.Reserved,
            OrderStatus.InUsing->ReservaTime.startDateTime()<=DateTime.now()
            else -> false
        }
    }
    //已經付完款的
    fun isCheckOut():Boolean{
        return when(orderStatus){
            OrderStatus.CheckOut->true
            else->false
        }
    }

    fun beCheckout(){
        orderStatus=OrderStatus.CheckOut
        CheckOutUrl=""
    }

    override fun copyFrom(from: EkiOrder): Boolean {
        SerialNumber=from.SerialNumber
        Cost=from.Cost
        Unit=from.Unit
        HandlingFee=from.HandlingFee
        Status=from.Status
        CarNum=from.CarNum
        CreatTime=from.CreatTime
        CheckOutUrl=from.CheckOutUrl
        Cp=from.Cp
        ReservaTime=from.ReservaTime
        Address=from.Address
        LocPrice=from.LocPrice
        LocSerial=from.LocSerial
        return true
    }

    override val costValue: Double
        get() = Cost
    override val currencyUnit: Int
        get() = Unit

    override fun startDateTime(from: DateTime): DateTime =ReservaTime.startDateTime()
    override fun endDateTime(from: DateTime): DateTime =Checkout?.Date?.toDateTime()?.standarByTimeOffset()?:ReservaTime.endDateTime()
    override fun equals(other: Any?): Boolean {
        return super<SqlVO>.equals(other)
    }
}

class Cp(): SqlVO<Cp>() {
    var Serial = ""

    override fun clear() {
        Serial = ""
    }
}