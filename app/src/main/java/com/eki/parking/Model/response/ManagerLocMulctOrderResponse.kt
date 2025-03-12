package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO

/**
 * Created by Hill on 2021/01/20
 */
class ManagerLocMulctOrderResponse:ResponseVO() {
    var info= listOf<MulctResult>()
    class MulctResult{
        var SerNum=""
        var Result= listOf<ResponseInfo.MulctOrder>()
    }
}