package com.eki.parking.Model.DTO

import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.extension.parseEnum
import com.hill.devlibs.impl.IConvertData
import com.hill.devlibs.impl.ISocket
import com.hill.devlibs.model.sql.SqlVO

class LocationSocket : SqlVO<LocationSocket>(),
    IConvertData<RequestBody.LocationSocket>,
    ISocket {

    var Current = 0
    var Charge = 0

    var currentEnum: CurrentEnum
        get() = parseEnum(Current)
        set(current) {
            Current = current.value
        }

    var chargeSocket: ChargeSocket
        get() = parseEnum(Charge)
        set(charge) {
            Charge = charge.value
        }

    override fun clear() {}

    override fun currentValue(): Int = Current

    override fun chargeValue(): Int = Charge

      override fun copyFrom(from: LocationSocket): Boolean =
          runCatching {
              Current= from.Current
              Charge = from.Charge
          }.isSuccess

    override fun toData(): RequestBody.LocationSocket =
        RequestBody.LocationSocket().also {
            it.current = Current
            it.charge = Charge
        }

}