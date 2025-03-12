package com.eki.parking.Model.DTO

import com.eki.parking.Model.EnumClass.ChargeSocket
import com.eki.parking.Model.EnumClass.CurrentEnum
import com.eki.parking.Model.request.body.VehicleBody
import com.hill.devlibs.annotation.GsonSkip
import com.hill.devlibs.impl.IConvertData

/**
 * Created by Hill on 2019/12/24
 */
/**
 * {
 * "Label":"BenZ",
 * "Type":"AE87",
 * "Current":1,
 * "Charge":2,
 * "Number":"ABC-123",
 * "Name":"今日我最狂",
 * "Img":"http://iparkingnet.ekiweb.com/upload/member/8475C0E5-BA1A-47F1-B046-DA2A6C4CEEB6/201910080514282401341.jpg",
 * "Id":20}
 */
data class VehicleInfo(var Label:String="",
                       var Type:String="",
                       var Current:Int=0,
                       var Charge:Int=0,
                       var Number:String="",
                       var Name:String="",
                       var Img:String="",
                       var IsDefault:Boolean=false,
                       var Id:Int=0):IConvertData<RequestBody.VehicleInfo>{

    @GsonSkip
    @JvmField
    val current:CurrentEnum =CurrentEnum.parse(Current)
    @GsonSkip
    @JvmField
    val chargeSocket:ChargeSocket =ChargeSocket.parse(Charge)

    fun update(info:VehicleInfo){
        Label=info.Label
        Type=info.Type
        Current=info.Current
        Charge=info.Charge
        Number=info.Number
        Name=info.Name
        Img=info.Img
        IsDefault=info.IsDefault
        Id=info.Id
    }

    override fun toData(): RequestBody.VehicleInfo =
            RequestBody.VehicleInfo(
                    id = Id,
                    name = Name,
                    number = Number,
                    label = Label,
                    type = Type,
                    current = Current,
                    charge = Charge,
                    isDefault = IsDefault)

}