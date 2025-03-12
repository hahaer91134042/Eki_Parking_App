package com.eki.parking.Model.DTO

import com.eki.parking.AppConfig
import com.eki.parking.Controller.impl.IFcmMsg
import com.eki.parking.Controller.impl.ISocketMsg
import com.eki.parking.Controller.socket.SocketMsg
import com.eki.parking.Model.EnumClass.ActionType
import com.eki.parking.Model.EnumClass.NotifyType
import com.eki.parking.Model.EnumClass.OrderStatus
import com.eki.parking.Model.sql.*
import com.eki.parking.Model.sql.bean.MemberInfo
import com.eki.parking.extension.parseEnum
import com.eki.parking.extension.toEnum
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IConvertToSql
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.tools.ContentPrinter
import java.io.Serializable

/**
 * Created by Hill on 2020/04/16
 */
object ResponseInfo{

    class MulctOrder:Serializable{
        var Start=""
        var End=""
        var CancelTime=""
        var Amt=0.0
    }

    class LocIncome : Serializable {
        var Start = ""
        var End = ""
        var Income=0.0
        var Claimant=0.0
    }

    class ManagerOrderCancel:Serializable{
        var SerialNumber=""
        var Success=false
        var Order:OrderResult?=null
        var Mulct:Mulct?=null
    }

    class Action:Serializable{
        var Serial=""
        var Type=0
        var discount:ActionDiscount?=null

        val type
            get() = Type.toEnum<ActionType>()
    }
    class ActionDiscount:Serializable{
        /*
        "Number": 0.0,//固定折抵的價格
        "Ratio": 0.8//打折數
        */
        var Number=0.0
        var Ratio=0.0
    }

    class Coupon:Serializable{
        var Code=""
        var Amt:Double=0.0
        var IsRange=false
        var End=""
    }

    class ServerNotify:Serializable{
        var Title=""
        var Msg=""
        var Html=""
    }
    class ActionNotify:Serializable{
        var Name=""
        var Page=ActionPage()
    }

    class ActionPage:Serializable{
        var Html=""
        var Url=""
    }

    class EkiNotify:Serializable {
        var Type=0
        var Action:ActionNotify?=null
        var Server:ServerNotify?=null
//        var Content:Serializable?=null

        val type
            get() = Type.toEnum<NotifyType>()
    }

    class ManagerLocOrder{
        var Id=0
        var SerNum=""
        var Order=ArrayList<OrderResult>()
    }

    class Bank:IConvertToSql<MemberBank>{
        var Name=""
        var Key=""
        var Bank=""
        var IsPerson=true
        var Address=AddressInfo()
        override fun toSql(): MemberBank =MemberBank().also {
            it.Name=Name
            it.Key=Key
            it.Bank=Bank
            it.IsPerson=IsPerson
            it.Address=Address
        }
    }

    class CancelOpenSet{
        var Location=ManagerLocation()
        var Mulcts=ArrayList<Mulct>()
    }

    class Mulct:Serializable,IConvertToSql<ManagerMulct>{
        var Amt=0.0
        var Unit=0
        var Paid=false
        var Time=""
        override fun toSql(): ManagerMulct = ManagerMulct().also {
            it.Amt=Amt
            it.Unit=Unit
            it.Paid=Paid
            it.Time=Time
        }
    }

    class OrderExtendTime{
        var Serial = ""
        var Order:OrderResult?=null
        var ReservaStatus:LoadReserva?=null
    }

    class OrderResult :IConvertToSql<EkiOrder>,IFcmMsg.Content,ISocketMsg.Content{
        var SerialNumber:String=""
        var Cost:Double=0.0
        var Unit:Int=0
        var LocPrice:Double=0.0
        var LocSerial=""
        var HandlingFee:Double=0.0
        var Status:Int=0
        var CarNum:String=""
        var CreatTime=""
        var Rating=false
        var Argue=false
        var CheckOutUrl=""
        var Cp = Cp()
        var Member:OrderMember?=null
        var ReservaTime=ReservaSet()
        var Address= AddressInfo()
        var Checkout:OrderCheckout?=null
        var Invoice:OrderInvoice?=null

        var orderStatus: OrderStatus
            set(value) { Status=value.value }
            get() = parseEnum(Status)
        var creatTime: DateTime
            private set(value) {}
            get() {
                return CreatTime.toDateTime(AppConfig.ServerSet.dateTimeFormat)
            }

        override fun toSql(): EkiOrder = EkiOrder().also { order->
            order.SerialNumber=SerialNumber
            order.Cost=Cost
            order.Unit=Unit
            order.HandlingFee=HandlingFee
            order.Status=Status
            order.CarNum=CarNum
            order.CreatTime=CreatTime
            order.Rating=Rating
            order.Argue=Argue
            order.CheckOutUrl=CheckOutUrl
            order.Cp=Cp
            order.ReservaTime=ReservaTime
            order.Address=Address
            order.LocPrice=LocPrice
            order.LocSerial=LocSerial
            order.Checkout=Checkout
            order.Member=Member
            order.Invoice=Invoice
        }

        override val socketContentType: SocketMsg.ContentType
            get() = SocketMsg.ContentType.ManagerOrderCancel
    }

    class OrderMember:Serializable{
        var Phone=""
        var NickName=""
    }

    class RegisterToken{
        var token=""
    }
    class ImgInfo{
        var imgUrl=""
    }
    class CheckOutUrl{
        var Url=""
    }

    class LoadReserva: IConvertToSql<LocationReserva> {

        override fun toSql(): LocationReserva = LocationReserva().also {
            it.LocId=Id
            it.LocNum=SerialNumber
            it.OpenList.addAll(OpenSet)
//            OpenSet.forEach { f-> it.OpenList.add(OpenSet().from(f)) }

            it.ReservaList.addAll(ReservaTime)
//            ReservaTime.forEach { r-> it.ReservaList.add(ReservaSet().from(r)) }
        }

        var Id=0
        var SerialNumber=""
        var OpenSet=ArrayList<OpenSet>()
        var ReservaTime=ArrayList<ReservaSet>()

    }

    class OrderAdd{
        var Time=OrderReservaTime()
        var Order: OrderResult?=null
        var LoadData: LoadReserva?=null
        var Success=false
    }

    class OrderCancel{
        var Remain=0
        var Result=ArrayList<CancelResult>()
    }
    class CancelResult{
        var SerNum=""
        var Success=false
        var Checkout=false
        var Order=OrderResult()
    }

    class OrderExtend{
        var Remain=0
        var Result=ArrayList<CancelResult>()
    }

    class OrderLinePay {
        var App = ""
        var Web = ""
    }

    class CreditAgree {
        var Serial = ""
        var Amt = 0
        var Card4 = ""
        var Time = ""
        var TokenLife = ""
    }

    class ExtendResult{
        var SerNum=""
        var Success=false
        var Checkout=false
        var Order=OrderResult()
    }

    class LoginInfo : SqlVO<LoginInfo>() {
        var token = ""
        var mail=""
        var phoneNum=""
        var beManager=false
        var referrer=""
        var Lv=0
        var member: MemberInfo? = null
        var address: AddressInfo? = null
        var vehicleList:ArrayList<VehicleInfo> =ArrayList()
        var payTokenLife:PayTokenLifeInfo? = null

        override fun clear() {
        }
        override fun printValue() {
            printContentValue(ContentPrinter()
                    .setKeys("token","beManager","Lv")
                    .setValues(token,beManager,Lv))
            member?.printValue()
            address?.printValue()
        }
    }

    class CheckCharge {
        var SerNum = ""
        var CpStatus = ""
    }
    class StartCharge {
        var SerNum = ""
        var Success = false
    }
}