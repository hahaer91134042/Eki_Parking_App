package com.eki.parking.Model.EnumClass

import com.eki.parking.Model.EnumClass.impl.IEnumValue

/**
 * Created by Hill on 06,11,2019
 */
enum class OrderStatus(override val value:Int): IEnumValue {
    FAIL(-1) {
        override val default: Boolean
            get() = true
    },
    //已預約
    Reserved(0) {
        override val default: Boolean
            get() = false
    },
    //使用中 目前技術上用不到
    InUsing(1) {
        override val default: Boolean
            get() = false
    },
    //待結帳 (已結清)
    BeSettle(2) {
        override val default: Boolean
            get() = false
    },
    //以結帳
    CheckOut(3) {
        override val default: Boolean
            get() = false
    },
    //已取消
    Cancel(4) {
        override val default: Boolean
            get() = false
    },
    //爭議訂單
    Disputed(5) {
        override val default: Boolean
            get() = false
    },
    //付款過程出現問題
    PayError(6) {
        override val default: Boolean
            get() = false
    },
    //地主因為關閉開放時間而取消
    CancelByManager(7) {
        override val default: Boolean
            get() = false
    };

    companion object{
        fun parse(value: Int): OrderStatus {
            values().forEach {
                when (it.value) {
                    value -> return it
                }
            }
            return FAIL
        }
    }
}