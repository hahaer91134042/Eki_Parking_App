package com.eki.parking.Model.DTO

import com.eki.parking.Model.EnumClass.BillingMethod
import com.eki.parking.Model.EnumClass.CurrencyUnit
import com.eki.parking.Model.EnumClass.OrderInvoiceType
import com.eki.parking.Model.EnumClass.WeekDay
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.DateUnit

/**
 * Created by Hill on 2020/04/15
 */
object RequestBody {

    class OrderCancelImgInfo{
        var serNum=""
    }

    /*
    * "invoice":{//可不帶 就由公司捐調
    	"name":"xxx",//人名 或者公司名稱
    	"ubn":"",//統一編號 個人免填 帶空字串
    	"address":"",//地址
    	"mail":"",//電子信箱
    	"type":1, //0~5
    	"carrierNum":"手機載具號碼or自然人憑證",
    	"loveCode":"捐贈愛心碼"
    }
    * */
    class OrderInvoice{
        var name=""
        var ubn=""
        var address=""
        var mail=""
        var type=OrderInvoiceType.Donate.value
        var carrierNum=""
        var loveCode=""
    }

    class OrderExtend{
        var serNum=""
        var time=""
    }

    class EditPwd{
        var oldPwd=""
        var newPwd=""
    }

    class ForgetPwd{
        var phone=""
        var pwd=""
    }

    class ArgueInfo{
        var serial=""
        var type=0
        var source=0
        var text=""
        var lat:Double=0.0
        var lng:Double=0.0
    }

    class TimeSpan(){
        constructor(s: DateUnit, e: DateUnit):this(){
            start=s.toString()
            end=e.toString()
        }
        constructor(s: DateTime, e: DateTime):this(){
            start=s.toString()
            end=e.toString()
        }
        var start=""
        var end=""
    }

    class CreditCard{
        var category=0
        var cardNum=""
        var limitDate=""
        var check=""
        var firstName=""
        var lastName=""
    }

    class BankInfo{
        var name=""
        var isPerson=true
        var serial=""
        var code=""
        var sub=""
        var account=""
        //var address=Address()
    }


    class MemberInfo {
        var firstName=""
        var lastName=""
        var nickName=""
        var countryCode=""
        var phone=""
    }

    class PayTokenInfo {
        var neweb = ""
    }

    class CreditAgreeInfo{
        var serial = ""
    }

    class Address {
        var country=""
        var state=""
        var city=""
        var detail=""
        var zip=""
    }

    class SetLocImg{
        var serNum=""
        var sort=0
    }

//    class AddLocation {
//        var lat:Double=0.0
//        var lng:Double=0.0
//        var address=Address()
//        var info=LocationInfo()
//        var config=LocationConfig()
//    }
//    class EditLocation {
//        var id=0
//        var info=LocationInfo()
//        var config=LocationConfig()
//    }

    class LocationInfo{
        var content=""
        var size=0
        var position=0
        var type=0
        var height:Double=-1.0
        var weight:Double=-1.0
    }

    class LocationConfig{
        var beRepeat=true
        var beEnable=true
        var text=""
        var price=0.0
        var unit= CurrencyUnit.TWD.value
        var method= BillingMethod.Per30Mins.value
        var openSet=ArrayList<Open>()
    }

    class LocationSocket{
        var current = 0
        var charge = 0
    }

    class Open{
        var week= WeekDay.NONE.value
        var date=""
        var start=""
        var end=""
    }
    data class VehicleInfo(
        var id: Int = 0,
        var name: String = "",
        var number: String = "",
        var label: String = "",
        var type: String = "",
        var current: Int = 0,
        var charge: Int = 0,
        var isDefault: Boolean = false)
}