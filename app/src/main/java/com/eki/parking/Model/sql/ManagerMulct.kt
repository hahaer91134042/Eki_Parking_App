package com.eki.parking.Model.sql

import com.eki.parking.Model.impl.ICurrencyCost
import com.eki.parking.Model.impl.IDbQueryArgs
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.util.DbUtil

/**
 * Created by Hill on 2020/04/10
 */
@SqlTableSet(table = "ManagerMulct")
class ManagerMulct:SqlVO<ManagerMulct>(), ICurrencyCost,IDbQueryArgs{

    @SqlColumnSet(key="Id",attr = SqlAttribute.ID,order = 1)
    var Id=0
    @SqlColumnSet(key="Amt",attr = SqlAttribute.DOUBLE,order = 2)
    var Amt=0.0
    @SqlColumnSet(key="Unit",attr = SqlAttribute.INT,order = 3)
    var Unit=0
    @SqlColumnSet(key="Paid",attr = SqlAttribute.BOOLEAN,order = 4)
    var Paid=false
    @SqlColumnSet(key="Time",attr = SqlAttribute.TEXT,order = 5)
    var Time=""


    override fun clear() {

    }

    override val costValue: Double
        get() = Amt
    override val currencyUnit: Int
        get() = Unit

    override fun queryBuilder(): IQueryArgsBuilder =object :IQueryArgsBuilder(){
        override val select: String
            get() = DbUtil.select("Id")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(Id)
        override val where: String
            get() = DbUtil.where("Id",Id)
    }
}