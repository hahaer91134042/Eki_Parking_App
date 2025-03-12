package com.eki.parking.Controller.activity.intent

import android.content.Context
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.OrderDetailActivity
import com.eki.parking.Model.sql.EkiOrder
import com.hill.devlibs.impl.IActivityIntent
import com.hill.devlibs.model.ValueObjContainer

/**
 * Created by Hill on 2020/05/19
 */
class OrderDetailIntent(from: Context,private var order: EkiOrder) : IActivityIntent(from),
                                                                     IActivityIntent.ForResultBack{
    override val target: Class<*>
        get() = OrderDetailActivity::class.java
    override val dataPair: DataPair?
        get() = object :DataPair{
            override val flag: String
                get() = AppFlag.DATA_FLAG
            override val data: ValueObjContainer<*>
                get() = ValueObjContainer<EkiOrder>().also { it.setValueObjData(order) }
        }
    override val requestCode: Int
        get() = 1
}