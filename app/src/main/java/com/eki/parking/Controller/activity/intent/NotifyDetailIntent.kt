package com.eki.parking.Controller.activity.intent

import android.content.Context
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.NotifyDetailActivity
import com.eki.parking.Model.DTO.ResponseInfo
import com.hill.devlibs.impl.IActivityIntent
import com.hill.devlibs.model.ValueObjContainer

/**
 * Created by Hill on 2020/07/27
 */
class NotifyDetailIntent(from: Context,var noti: ResponseInfo.EkiNotify) : IActivityIntent(from) {

    override val target: Class<*>
        get() = NotifyDetailActivity::class.java

    override val dataPair: DataPair?
        get() = object :DataPair{
            override val flag: String
                get() = AppFlag.DATA_FLAG
            override val data: ValueObjContainer<*>
                get() = ValueObjContainer<ResponseInfo.EkiNotify>().also { it.setValueObjData(noti) }
        }
}