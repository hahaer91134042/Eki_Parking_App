package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO

/**
 * Created by Hill on 2020/08/31
 */
class ManagerOrderCancelResponse:ResponseVO() {

    var info=ArrayList<ResponseInfo.ManagerOrderCancel>()

}