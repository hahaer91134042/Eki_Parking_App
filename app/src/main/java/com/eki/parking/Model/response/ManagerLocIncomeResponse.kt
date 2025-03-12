package com.eki.parking.Model.response

import com.eki.parking.Model.DTO.ResponseInfo
import com.eki.parking.Model.ResponseVO
import java.io.Serializable

/**
 * Created by Hill on 2021/01/15
 */
class ManagerLocIncomeResponse:ResponseVO() {

    var info= arrayListOf<IncomeResult>()

    class IncomeResult:Serializable{
        var SerNum=""
        var Result= arrayListOf<ResponseInfo.LocIncome>()
    }

}