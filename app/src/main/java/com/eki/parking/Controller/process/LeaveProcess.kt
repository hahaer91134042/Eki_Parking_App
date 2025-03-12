package com.eki.parking.Controller.process
import com.eki.parking.Model.sql.LocationReserva
import com.eki.parking.Model.sql.ManagerLocation

/**
 * Created by Hill on 2020/02/24
 */
class LeaveProcess : BaseProcess(null) {
    override fun run() {
        sqlManager.clean(LocationReserva::class.java)
        sqlManager.clean(ManagerLocation::class.java)
    }
}