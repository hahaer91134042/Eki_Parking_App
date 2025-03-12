package com.eki.parking.Model.sql

import com.eki.parking.Model.impl.IDbQueryArgs
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.Model.DTO.ReservaSet
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.util.DbUtil

/**
 * Created by Hill on 31,10,2019
 */
@SqlTableSet(table = "LocationReserva",isClear = false)
class LocationReserva : SqlVO<LocationReserva>(), IDbQueryArgs {

    override fun queryBuilder(): IQueryArgsBuilder =object :IQueryArgsBuilder(){
        override val select: String
            get() = DbUtil.select("LocId")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(LocId.toString())
        override val where: String
            get() = DbUtil.where("LocId",LocId.toString())
    }

    @SqlColumnSet(key="Id",attr = SqlAttribute.ID,order = 1)
    var Id=0
    @SqlColumnSet(key="LocId",attr = SqlAttribute.INT,order = 2)
    var LocId=0
    @SqlColumnSet(key="LocNum",attr = SqlAttribute.TEXT,order = 3)
    var LocNum=""
    @SqlColumnSet(key="OpenList",attr = SqlAttribute.Array,order = 4) //可預約時間
    var OpenList=ArrayList<OpenSet>()
    @SqlColumnSet(key="ReservaList",attr = SqlAttribute.Array,order = 5) //已預約時間
    var ReservaList=ArrayList<ReservaSet>()

    override fun clear() {

    }

    override fun printValue() {
        printObj(this)
        OpenList.forEach { it.printValue() }
        ReservaList.forEach { it.printValue() }
    }

}