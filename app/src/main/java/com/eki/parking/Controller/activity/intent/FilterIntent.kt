package com.eki.parking.Controller.activity.intent

import android.content.Context
import com.eki.parking.AppFlag
import com.eki.parking.AppRequestCode
import com.eki.parking.Controller.activity.FilterActivity
import com.eki.parking.Model.sql.LoadLocationConfig
import com.hill.devlibs.impl.IActivityIntent
import com.hill.devlibs.model.ValueObjContainer

/**
 * Created by Hill on 2021/12/07
 */
class FilterIntent(from: Context,private var config:LoadLocationConfig) : IActivityIntent(from),
                                    IActivityIntent.ForResultBack {
    override val target: Class<*>
        get() = FilterActivity::class.java
    override val dataPair: DataPair?
        get() = object :DataPair{
            override val flag: String
                get() = AppFlag.DATA_FLAG
            override val data: ValueObjContainer<*>
                get() = ValueObjContainer<LoadLocationConfig>().also {
                    it.setValueObjData(config)
                }
        }
    override val requestCode: Int
        get() = AppRequestCode.OnFilter
}