package com.eki.parking.Model.response

import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.sql.ManagerLvPercent

/**
 * Created by Hill on 2021/01/21
 */
class ManagerLvInfoResponse:ResponseVO() {

    var info=ManagerLvPercent()

}