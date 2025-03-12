package com.eki.parking.Controller.activity.intent

import android.content.Context
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.SiteDetailActivity
import com.eki.parking.Model.sql.ManagerLocation
import com.hill.devlibs.impl.IActivityIntent
import com.hill.devlibs.model.ValueObjContainer

/**
 * Created by Hill on 2020/03/10
 */
class SiteDetailIntent(from: Context,private var loc:ManagerLocation) : IActivityIntent(from) {
    override val target: Class<*>
        get() = SiteDetailActivity::class.java
    override val dataPair: DataPair?
        get() = object :DataPair{
            override val flag: String
                get() = AppFlag.DATA_FLAG
            override val data: ValueObjContainer<*>
                get() = ValueObjContainer<ManagerLocation>().apply { setValueObjData(loc) }
        }
}