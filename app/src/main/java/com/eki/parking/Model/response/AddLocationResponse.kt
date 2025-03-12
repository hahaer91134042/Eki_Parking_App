package com.eki.parking.Model.response

import com.eki.parking.Model.ResponseVO
import com.eki.parking.Model.sql.ManagerLocation

/**
 * Created by Hill on 2020/04/14
 */
class AddLocationResponse:ResponseVO() {
    var info=ManagerLocation()
}