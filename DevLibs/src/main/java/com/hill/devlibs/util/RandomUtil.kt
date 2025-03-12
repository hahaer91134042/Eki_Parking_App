package com.hill.devlibs.util

import androidx.annotation.IntRange
import java.util.*


/**
 * Created by Hill on 2018/12/25.
 * 給測試用來生亂數使用
 */
object RandomUtil {


    fun randomInt(@IntRange(from = 1,to = 8) length: Int):Int{
        return randomString(length,RandomTemplete.number).toInt()
    }

    fun randomString(length:Int,input:RandomTemplete=RandomTemplete.all):String{
        var builder=StringBuilder()
        var ran=Random()
        var temp=input.templete
        for (i in 0 until length){
            builder.append(temp[ran.nextInt(temp.length)])
        }

        return builder.toString()
    }


    private var charTemplete = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private var numTemplete = "0123456789"

    private fun getRandomInvoiceNum(long: Int): String {


        var invoiceNum = StringBuilder("")

        var random = Random()
        while (invoiceNum.length < long) {
            if (invoiceNum.length < 2) {
                invoiceNum.append(charTemplete[random.nextInt(26)])//0~25
            } else {
                invoiceNum.append(numTemplete[random.nextInt(10)])
            }
        }
        return invoiceNum.toString()
    }
}