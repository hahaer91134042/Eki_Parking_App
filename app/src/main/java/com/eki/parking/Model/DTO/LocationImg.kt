package com.eki.parking.Model.DTO

import com.hill.devlibs.impl.IImgUrl
import com.hill.devlibs.model.sql.SqlVO

/**
 * Created by Hill on 2020/09/24
 */
class LocationImg:SqlVO<LocationImg>(),IImgUrl {

    var Sort=0
    var Url=""


    override fun clear() {

    }

    override fun imgUrl(): String =Url
}