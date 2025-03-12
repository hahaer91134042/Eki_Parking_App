package com.eki.parking.Controller.sql

import android.net.Uri
import com.hill.devlibs.sql.SqlPair

/**
 * Created by Hill on 2019/6/14
 */
class SqlUriPair(clazz: Class<*>) : SqlPair(clazz) {
    var uri = Uri.parse("content://${SqlConfig.AUTHORITY}/$tableName")
}