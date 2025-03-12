package com.eki.parking

import com.eki.parking.extension.raw
import com.hill.devlibs.EnumClass.DistanceUnit
import com.hill.devlibs.impl.IHttpsSet
import com.hill.devlibs.socket.ISocketConfig
import java.io.InputStream

/**
 * Created by Hill on 2019/7/31
 */

object AppConfig {

    const val cameraDir="EkiCamera"
    const val privateKey="!qaz2WSX#edc"
    const val EkiScheme="Eki"
    const val notifyChannelId="EkiChannel"
    const val maxReservaDay=60
    const val maxOrderDay=90//最多查詢90天之前的訂單
    const val maxSearchInfoRecord=5
    const val spendHourPerMonth=0//單月花費時間
    const val maxOpenHourPerMonth=240//單月最高240小時

    const val ReservaDelayMin=15 //預約之間的間格至少15分鐘
    const val ReservaGapMin=30
    const val ReservaTimeOffsetMin=15
    const val FreeCheckoutMinute=10
    const val maxCarSetNumber=5
    const val openGapMin=30 //最小開放時間(分鐘)
    const val openOffsetMin=15

//    const val ReservaCancelMin=30//30分鐘以上才能取消
    const val ReservaCancelFreeMin=30//30分鐘以上才能免費取消
    const val markerRefreshTime=500L
    const val openOrderExtenMin=15//延長訂單最少在要結束前15分鐘內
//    @JvmStatic
//    val appMaxSearch=MaxSearchRange()

    object MaxSearchRange{
        const val range=350000
        @JvmStatic
        val unit= DistanceUnit.M
    }
    object ServerSet{
        const val dateFormat="yyyy-MM-dd"
        const val timeFormate="HH:mm:ss"
        const val dateTimeFormat="yyyy-MM-dd HH:mm:ss"
        const val dateTimeFormat2="yyyy/MM/dd HH:mm:ss"
        const val orderClaimantRate=3.0
    }
    object AnimateFile{
        const val opening="opening02.json"
        const val adstore="adstore.json"
    }
    object HttpSet{
        val ppyp=object : IHttpsSet.IAspNetHttps(){
            override fun keyRaw(): InputStream = raw(R.raw.ppyp_cer)
            override fun hostName(): String ="api.ppyp.app"
            override fun sslPwd(): String ="xcvUJM*)"
        }
    }
    object Url{
        const val web="https://www.ppyp.app"
//        const val web="http://iparkingnet.eki.com.tw"

        //正式版使用
        const val api="https://api.ppyp.app/api"
        //測試版
//        const val api="http://iparkingnet.eki.com.tw/api"

        //正式
        var AD_URL = "https://www.ppyp.app/AD/"
        //測試
        //var AD_URL = "https://www.ppyp.app/AD/Test/"

        //車主常見問題
        const val memberQuestion="https://www.ppyp.app/tc/faq_d_content.html"
        //地主常見問題
        const val managerQuestion="https://www.ppyp.app/tc/faq_l_content.html"
        //隱私權政策
        const val policyLink="https://www.ppyp.app/tc/policy_content.html"
        //社群守則
        const val regulationLink="https://www.ppyp.app/tc/regulation_d_content.html"
        //會員服務條款
        const val appMemberLink="https://www.ppyp.app/tc/rule_d_content.html"

    }

    object Invoice{
        val defautLoveCode="7505"
    }

    object Value{
        //正式版使用
        const val smsCheck=""
        //測試版
//        const val smsCheck="1234"
    }
    object Socket{
        //正式版
        val config=object :ISocketConfig{
            override val server: String
                get() = "ws://api.ppyp.app"
            override val port: Int
                get() = 5000
            override val path: String
                get() = ""
        }
        //測試版
//        val config=object :ISocketConfig{
//            override val server: String
//                get() = "ws://iparkingnet.eki.com.tw"
//            override val port: Int
//                get() = 3000
//            override val path: String
//                get() = ""
//        }
    }
}