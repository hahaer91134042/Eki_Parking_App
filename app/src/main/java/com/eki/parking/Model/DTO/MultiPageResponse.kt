package com.eki.parking.Model.DTO

/**
 * Created by Hill on 07,10,2019
 */
data class MultiPageResponse<DATA>(val page:Int,var response:DATA) {
}