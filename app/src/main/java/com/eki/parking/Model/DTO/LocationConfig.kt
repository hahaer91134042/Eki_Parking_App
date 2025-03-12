package com.eki.parking.Model.DTO

import com.eki.parking.Controller.impl.IPriceSet
import com.eki.parking.Model.EnumClass.BillingMethod
import com.eki.parking.Model.EnumClass.CurrencyUnit
import com.eki.parking.extension.parseEnum
import com.hill.devlibs.impl.IConvertData
import com.hill.devlibs.model.sql.SqlVO


/**
 * Created by Hill on 25,09,2019
 */
class LocationConfig : SqlVO<LocationConfig>(),
                       IConvertData<RequestBody.LocationConfig>,
                       IPriceSet<Double>{

    var beRepeat=true
    var beEnable = true
    var Text: String? = ""
    var Price:Double= 0.0

    var Unit = 0
    var Method = 0

    var OpenSet = ArrayList<OpenSet>()

    var currencyUnit
        get() = CurrencyUnit.parse(Unit)
        set(value) {
            Unit = value.value
        }
    var billingMethod:BillingMethod
        get() = parseEnum(Method)
        set(value) {
            Method = value.value
        }

    override fun copyFrom(from: LocationConfig): Boolean =
            runCatching {
                beRepeat=from.beRepeat
                beEnable=from.beEnable
                Text=from.Text
                Price=from.Price
                Unit=from.Unit
                Method=from.Method
                OpenSet.clear()
                OpenSet.addAll(from.OpenSet)
            }.isSuccess

    override fun clear() {

    }

    override fun printValue() {
        printObj(this)
//        printListData(OpenSet)
    }

    override fun toData(): RequestBody.LocationConfig =
            RequestBody.LocationConfig().also {
                it.beRepeat=beRepeat
                it.beEnable=beEnable
                it.method=Method
                it.price=Price
                it.text=Text?:""
                it.unit=Unit
                OpenSet.forEach {set->
                    it.openSet.add(set.toData())
                }
            }

    override fun price(): Double =Price

    override fun billingMethod(): BillingMethod =billingMethod
}