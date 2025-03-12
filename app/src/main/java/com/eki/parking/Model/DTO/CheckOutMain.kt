package com.eki.parking.Model.DTO

import com.eki.parking.Model.response.LoadReservaResponse
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.LocationReserva
import java.io.Serializable

class CheckOutMain(var arrEkiOrder: ArrayList<EkiOrder>, var reserva: LocationReserva, var loadReservaResponse: LoadReservaResponse, var ekiOrder: EkiOrder) : Serializable