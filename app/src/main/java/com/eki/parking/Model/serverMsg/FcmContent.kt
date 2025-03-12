package com.eki.parking.Model.serverMsg

import com.eki.parking.Controller.impl.IFcmMsg

/**
 * Created by Hill on 2020/09/02
 */

object FcmContent {

    val msgList = arrayListOf<IFcmMsg<*>>(
        ManagerOrderCancelMsg(),
        CheckoutMsg(),
        GetOrderMsg(),
        CancelOrderMsg(),
        ManagerGetPayMsg(),
        OrderNoCheckOut(),
        ManagerBankTransfer(),
        PaySumReport(),
        OrderExtend()
    )

}