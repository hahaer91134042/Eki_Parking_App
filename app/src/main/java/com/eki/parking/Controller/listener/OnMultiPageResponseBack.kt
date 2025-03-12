package com.eki.parking.Controller.listener

import com.eki.parking.Model.DTO.MultiPageResponse

/**
 * Created by Hill on 18,10,2019
 */
interface OnMultiPageResponseBack<T>:OnResponseListener<ArrayList<MultiPageResponse<T>>>{
}