package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2019/6/21
 */
class PostImgResponse :ResponseVO(){

    var info:ResponseInfo.ImgInfo?=null


    override fun printValue() {
        super.printValue()
        Log.w("imgUrl->${info?.imgUrl}")
    }
}