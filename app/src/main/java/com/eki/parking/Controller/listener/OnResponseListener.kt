package com.eki.parking.Controller.listener

import com.hill.devlibs.listener.OnFailListener
//import com.eki.parking.model.response.EkiResponse
import com.hill.devlibs.listener.OnPostExecuteListener


/**
 * Created by Hill on 2019/6/20
 */
interface OnResponseListener<R> :OnPostExecuteListener<R>,OnFailListener{
    fun onReTry()
}