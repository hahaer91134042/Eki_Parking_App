package com.eki.parking.Model.EnumClass

/**
 * Created by Hill on 2021/02/03
 */
enum class BroadcastMethod(var str: String) {
    CancelOrder("CancelOrder"),
    GetCheckOut("GetCheckOut"),
    GetOrder("GetOrder"),
    ManagerCancelOrder("ManagerCancelOrder"),
    ManagerGetPay("ManagerGetPay"),
    OrderNoCheckOut("OrderNoCheckOut"),
    ManagerBankTransfer("ManagerBankTransfer"),
    PaySumReport("PaySumReport"),
    OrderExtend("OrderExtend")
}