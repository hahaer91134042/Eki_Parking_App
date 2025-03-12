package com.hill.devlibs.model.bean

/**
 * Created by Hill on 27,09,2019
 */
class PermissionSet(var name:String,var code:Int){
}

fun ArrayList<PermissionSet>.toCheckArray():Array<String>{
    var list=ArrayList<String>()
    this.forEach {
        list.add(it.name)
    }
    return list.toTypedArray()
}