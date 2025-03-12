package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.App
import com.eki.parking.Controller.impl.IMultiOverCheck
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.manager.SQLManager
import com.eki.parking.Model.request.EkiRequest
import com.hill.devlibs.model.bean.ServerRequestBody

/**
 * Created by Hill on 2019/12/09
 */

abstract class BaseProcess @JvmOverloads constructor(val from:Context?=null):IMultiOverCheck(),Runnable{

    interface ISetRequestBody<T:ServerRequestBody>{
        val body:T
        val request:EkiRequest<T>
    }
    interface ISetResponseListener<T:OnResponseListener<*>>{
        val onResponse:T?
    }
    interface ISetProcessBack{
        fun onProcessOver()
    }
    open fun onProcessFail(){}

    override fun onOver() {
        super.onOver()
        if (this is ISetProcessBack){
            this.onProcessOver()
        }
    }

    protected val app: App = App.getInstance()
    protected val sqlManager: SQLManager =app.sqlManager

}