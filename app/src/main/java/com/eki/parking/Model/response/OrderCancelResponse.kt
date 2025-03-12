package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO

/**
 * Created by Hill on 2020/02/18
 */
class OrderCancelResponse:ResponseVO() {

    var info=ResponseInfo.OrderCancel()

}