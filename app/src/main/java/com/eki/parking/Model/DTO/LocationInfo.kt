package com.eki.parking.Model.DTO

import com.eki.parking.Model.EnumClass.*
import com.eki.parking.extension.parseEnum
import com.hill.devlibs.impl.IConvertData
import com.hill.devlibs.model.sql.SqlVO

class LocationInfo:SqlVO<LocationInfo>(), IConvertData<RequestBody.LocationInfo>{

    var SerialNumber:String?=""
    var Content:String?=""
    var Img:String?=""
    var Position=0
    var Size=0
    var Type=0
    var Height:Double=-1.0
    var Weight:Double=-1.0

    var siteType: SiteType
        get() = parseEnum(Type)
        set(type) {
            Type = type.value
        }

    var sitePosition: SitePosition
        get() = parseEnum(Position)
        set(value) {
            Position = value.value
        }
    var siteSize: SiteSize
        get() = parseEnum(Size)
        set(value) {
            Size = value.value
        }

    override fun printValue() {
        printObj(this)
    }

    override fun clear() {

    }

    override fun copyFrom(from: LocationInfo): Boolean =
            runCatching {
                SerialNumber=from.SerialNumber
                Content=from.Content
                Img=from.Img
                Position=from.Position
                Size=from.Size
                Height=from.Height
                Weight=from.Weight
                Type=from.Type
            }.isSuccess

    override fun toData(): RequestBody.LocationInfo =
        RequestBody.LocationInfo().also {
            it.content=Content?:""
            it.size=Size
            it.position=Position
            it.weight=Weight
            it.height=Height
            it.type=Type
        }

}