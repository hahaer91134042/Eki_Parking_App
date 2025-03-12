package com.eki.parking.Controller.tools

import com.eki.parking.AppConfig
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.Model.impl.ICurrencyCost
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.extension.standarToCheckOut
import com.eki.parking.extension.timeSpan
import com.eki.parking.extension.toCurrency
import com.hill.devlibs.extension.floor
import com.hill.devlibs.extension.multiply
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.hourSpan

/**
 * Created by Hill on 2020/05/20
 */
object EkiCalculator {
    interface ICalRemainHour{
        val setTime:DateTime//這目前暫時沒太多用處因為進來的openSet 都不是週期性的
        val openSet:List<OpenSet>
        val pastOrder:List<EkiOrder>
    }

    fun ICalRemainHour.remainHour(): Double{
        var now=DateTime.now()
        var hourSpan = AppConfig.maxOpenHourPerMonth.hourSpan
        pastOrder.forEach { order->
            var oSpan=order.timeSpan
//            Log.w("Order serNum->${order.SerialNumber} start->${order.startDateTime()} end->${order.endDateTime()} span->$oSpan")
            hourSpan-=oSpan
        }

        return this.let {

            it.openSet.filter { set -> set.startDateTime(setTime).date >= now.date }
                    .forEach { set ->
//                        set.printValue()
//                        Log.d("set->$set start->${set.startDateTime(setTime)} timeSpan->${set.timeSpan}")
                        hourSpan -= set.timeSpan
//                        Log.i("remain hour->${hourSpan.totalHour}")
                    }
            hourSpan.totalHour.floor()
        }
    }

    class MultResult(order:EkiOrder,now:DateTime):ICurrencyCost{

        var amt:Double=order.let {
            var a=0.0
            a
        }
        var unit=order.Unit


        var time=now.toString()

        override val costValue: Double
            get() = amt
        override val currencyUnit: Int
            get() = unit

    }

    fun calCheckout(order: EkiOrder, checkoutTime: DateTime):OrderCheckOutResult=
            OrderCheckOutResult(order, checkoutTime)

    class OrderCheckOutResult(order: EkiOrder, checkoutTime: DateTime){
        var startTime=order.ReservaTime.startDateTime()
        var endTime=order.ReservaTime.endDateTime()
        var standarCheckOut=checkoutTime.standarToCheckOut(order.ReservaTime)
//                .also { Log.w("EkiCalculator start->$startTime end->$endTime standarCheckout time->$it stamp->${it.toStamp()}") }
        var hourPrice=order.LocPrice.multiply(2.0)

//        var normalSpan= when{
//            standarCheckOut<=endTime->standarCheckOut - startTime
//            else->endTime-startTime
//        }
        var normalSpan= when{
            standarCheckOut<=endTime->standarCheckOut - startTime
            else->endTime-startTime
        }

        var normalDuringHour= (normalSpan/1.hourSpan)
//                .also { Log.i("EkiCalculator normal span->$normalSpan duringHour->$it") }

        var normalCost=(hourPrice.multiply(normalDuringHour)).toCurrency(order.Unit)

        var damageDuringHour=when{
            standarCheckOut>=endTime->(standarCheckOut-endTime)/1.hourSpan
            else->0.0
        }
        //違約時間的金額
        var damageCost=((hourPrice.multiply(damageDuringHour)).multiply(AppConfig.ServerSet.orderClaimantRate)).toCurrency(order.Unit)

        var discountAmt=0.0//基本上是負值
        set(value) {
            finalCost -= value
            field=value
        }
        //違約金
//        var damage=damageCost-(order.LocPrice*damageDuringHour*2).toCurrency(order.Unit)
        var finalCost=(normalCost+damageCost-discountAmt).toCurrency(order.Unit)//最終總金額(主要以這個為準)
//        var price=order.

//        var totalDuringHour=(standarCheckOut-startTime)/1.hourSpan
        var totalDuringHour=(standarCheckOut-startTime)/1.hourSpan
        //var totalCost=(order.LocPrice*totalDuringHour*2).toCurrency(order.Unit)
        //var damagePart=finalCost-totalCost

        //這是計算地主所得 所以不能計算罰金倍率
        var totalNormalCost=(hourPrice.multiply(totalDuringHour)).toCurrency(order.Unit)
        var normalDamageCost=(hourPrice.multiply(damageDuringHour)).toCurrency(order.Unit)
        var finalNormalCost=(hourPrice.multiply(totalDuringHour)-discountAmt).toCurrency(order.Unit)
    }
}