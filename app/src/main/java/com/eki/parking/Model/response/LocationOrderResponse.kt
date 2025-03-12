package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO

/**
 * Created by Hill on 2020/05/08
 */
class LocationOrderResponse:ResponseVO(){
    var info=ArrayList<ResponseInfo.ManagerLocOrder>()
}