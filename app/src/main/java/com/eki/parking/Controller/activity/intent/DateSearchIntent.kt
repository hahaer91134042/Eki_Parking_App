package com.eki.parking.Controller.activity.intent

import android.content.Context
import com.eki.parking.AppFlag
import com.eki.parking.AppRequestCode
import com.eki.parking.Controller.activity.DateSearchActivity
import com.eki.parking.Model.DTO.LocationSearchTime
import com.hill.devlibs.impl.IActivityIntent
import com.hill.devlibs.model.ValueObjContainer

/**
 * Created by Hill on 2021/12/13
 */
class DateSearchIntent(from: Context,private var time:LocationSearchTime) : IActivityIntent(from),
                                        IActivityIntent.ForResultBack {
    override val target: Class<*>
        get() = DateSearchActivity::class.java
    override val dataPair: DataPair?
        get() = object :DataPair{
            override val flag: String
                get() = AppFlag.DATA_FLAG
            override val data: ValueObjContainer<*>
                get() = ValueObjContainer<LocationSearchTime>()
                    .also { it.setValueObjData(time) }
        }
    override val requestCode: Int
        get() = AppRequestCode.OnDateSearch
}