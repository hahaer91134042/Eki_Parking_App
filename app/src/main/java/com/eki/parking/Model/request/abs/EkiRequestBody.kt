package com.eki.parking.Model.request.abs

import com.eki.parking.Model.impl.IRequestApi
import com.hill.devlibs.model.bean.ServerRequestBody

/**
 * Created by Hill on 2019/4/24
 */
abstract class EkiRequestBody: ServerRequestBody(), IRequestApi {

    override fun clear() {
    }

    override fun printValue()=printObj(this)
}