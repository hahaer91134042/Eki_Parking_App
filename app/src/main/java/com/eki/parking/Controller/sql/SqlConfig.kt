package com.eki.parking.Controller.sql

import com.eki.parking.Model.sql.*
import com.hill.devlibs.collection.SqlList

/**
 * Created by Hill on 2019/6/14
 */
object SqlConfig {
    const val DB_NAME="EkiParking.db"
    const val DB_VERSION=74
    const val AUTHORITY = "com.eki.parking.Controller.sql.DBContentProvider"
    @JvmStatic
    val sqlTableList: SqlList<SqlUriPair> by lazy {
        return@lazy SqlList<SqlUriPair>().apply {
            add(SqlUriPair(EkiMember::class.java))
            add(SqlUriPair(EkiLocation::class.java))
            add(SqlUriPair(LoadLocationConfig::class.java))
            add(SqlUriPair(SearchInfo::class.java))
            add(SqlUriPair(LocationReserva::class.java))
            add(SqlUriPair(EkiOrder::class.java))
            add(SqlUriPair(CountryCode::class.java))
            add(SqlUriPair(CarBrand::class.java))
            add(SqlUriPair(BankCode::class.java))
            add(SqlUriPair(ManagerLocation::class.java))
            add(SqlUriPair(ManagerMulct::class.java))
            add(SqlUriPair(MemberBank::class.java))
            add(SqlUriPair(MemberLoginInfo::class.java))
            add(SqlUriPair(ManagerLvPercent::class.java))
//            add(SqlUriPair(ManagerLocIncome::class.java))
            add(SqlUriPair(ExtendedOrder::class.java))
        }

    }

}