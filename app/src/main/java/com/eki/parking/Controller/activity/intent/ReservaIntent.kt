package com.eki.parking.Controller.activity.intent

import android.content.Context
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.ReservaActivity
import com.eki.parking.Model.sql.EkiLocation
import com.hill.devlibs.impl.IActivityIntent
import com.hill.devlibs.model.ValueObjContainer

/**
 * Created by Hill on 2020/10/06
 */
class ReservaIntent(from: Context,private var loc:EkiLocation) : IActivityIntent(from) {
    override val target: Class<*>
        get() = ReservaActivity::class.java

    override val dataPair: DataPair?
        get() = object :DataPair{
            override val flag: String
                get() = AppFlag.DATA_FLAG
            override val data: ValueObjContainer<*>
                get() = ValueObjContainer<EkiLocation>().setValueObjData(loc)
        }

}