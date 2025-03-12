package com.eki.parking.Model.request

import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.body.SendIdBody

/**
 * Created by Hill on 2020/01/10
 */
class DeleteVehicleRequest:EkiRequest<SendIdBody>(){
    init {
        body= SendIdBody(EkiApi.DeleteVehicle)
    }
}