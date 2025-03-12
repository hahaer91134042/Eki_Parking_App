package com.eki.parking.Model.sql

import com.eki.parking.Model.impl.IDbQueryArgs
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.util.DbUtil

/**
 * Created by Hill on 2021/12/29
 * 這用來記錄已經延時過的訂單序號
 * 目前延時功能 只能延長一次
 */
@SqlTableSet(table = "ExtendedOrder")
class ExtendedOrder: SqlVO<ExtendedOrder>(),IDbQueryArgs {

    companion object {
        fun creatBy(order:EkiOrder):ExtendedOrder=ExtendedOrder().also {
            it.OrderSerial=order.SerialNumber
            it.cDate=DateTime.now().toString()
        }
    }

    @SqlColumnSet(key="OrderSerial",attr = SqlAttribute.TEXT,order = 1)
    var OrderSerial:String=""
    @SqlColumnSet(key="cDate",attr = SqlAttribute.TEXT,order = 2)
    var cDate:String=""


    override fun clear() {

    }

    override fun queryBuilder(): IQueryArgsBuilder=object :IQueryArgsBuilder(){
        override val select: String
            get() = DbUtil.select("OrderSerial")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(OrderSerial)
        override val where: String
            get() = DbUtil.where("OrderSerial",OrderSerial)
    }

}