package com.eki.parking.Model.model

import com.eki.parking.Model.DTO.*
import com.eki.parking.Model.impl.ICurrencyCost
import com.eki.parking.Model.impl.IDbQueryArgs
import com.eki.parking.Model.impl.ITimeConvert
import com.eki.parking.Model.sql.Cp
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.extension.standarByTimeOffset
import com.hill.devlibs.extension.toDateTime
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.util.DbUtil

data class EkiOrder2(
    var Address: Address,
    var Argue: Boolean,
    var CarNum: String,
    var CheckOutUrl: String,
    var Checkout: Checkout? = null,
    var Cost: Double,
    var Cp: Cp?,
    var CreatTime: String,
    var HandlingFee: Double,
    var Id: Int,
    var Invoice: Invoice? = null,
    var Member: ResponseInfo.OrderMember? = null,
    var LocPrice: Int,
    var LocSerial: String,
    var Rating: Boolean,
    var ReservaTime: ReservaTime,
    var SerialNumber: String,
    var Status: Int,
    var Unit: Int
) : SqlVO<EkiOrder>(), IDbQueryArgs, ICurrencyCost, ITimeConvert {
    override val costValue: Double
        get() = this.Cost.toDouble()
    override val currencyUnit: Int
        get() = Unit

    override fun clear() {

    }

    override fun queryBuilder(): IQueryArgsBuilder = object : IQueryArgsBuilder() {
        override val select: String
            get() = DbUtil.select("SerialNumber")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(SerialNumber)
        override val where: String
            get() = DbUtil.where("SerialNumber", SerialNumber)
    }

    override fun startDateTime(from: DateTime): DateTime = ReservaTime.startDateTime()

    override fun endDateTime(from: DateTime): DateTime =
        Checkout?.Date?.toDateTime()?.standarByTimeOffset() ?: ReservaTime.endDateTime()

    override fun copyFrom(from: EkiOrder): Boolean {
        SerialNumber = from.SerialNumber
        Cost = from.Cost
        Unit = from.Unit
        HandlingFee = from.HandlingFee
        Status = from.Status
        CarNum = from.CarNum
        CreatTime = from.CreatTime
        CheckOutUrl = from.CheckOutUrl
        Cp = this.Cp.apply { from.Cp?.Serial }
        ReservaTime = this.ReservaTime.apply { from.ReservaTime }
        Address = this.Address.apply { from.Address }
        LocPrice = this.LocPrice.apply { from.LocPrice }
        LocSerial = from.LocSerial
        return true
    }
}

fun EkiOrder2.toEkiOrder():EkiOrder {
    val order = this
    return EkiOrder().apply {
        this.Id = order.Id
        this.SerialNumber = order.SerialNumber
        this.Cost = order.Cost
        this.Unit = order.Unit
        this.HandlingFee = order.HandlingFee
        this.Status = order.Status
        this.CarNum = order.CarNum
        this.CreatTime = order.CreatTime
        this.Rating = order.Rating
        this.Argue = order.Argue
        this.CheckOutUrl = order.CheckOutUrl
        this.Cp = Cp().apply { this.Serial = order.Cp?.Serial ?: "" }
        this.ReservaTime = ReservaSet().apply {
            this.StartTime = order.ReservaTime.StartTime
            this.EndTime = order.ReservaTime.EndTime
            this.Remark = order.ReservaTime.Remark
            this.IsUser = order.ReservaTime.IsUser }
        this.Address = AddressInfo().apply {
            this.Country = order.Address.Country
            this.State = order.Address.State
            this.City = order.Address.City
            this.Detail = order.Address.Detail
            this.ZipCode = order.Address.ZipCode
        }
        order.Checkout?.let { checkout ->
            this.Checkout = OrderCheckout().apply {
                this.Date = checkout.Date
                this.CostFix = checkout.CostFix.toDouble()
                this.Claimant = checkout.Claimant.toDouble()
                this.Img = checkout.Img
            }
        }

        this.LocPrice = order.LocPrice.toDouble()
        this.LocSerial = order.LocSerial
        order.Invoice?.let { invoice ->
            this.Invoice = OrderInvoice().apply {
                this.Number = invoice.Number
                this.Card4No = invoice.Card4No
            }
        }
    }
}