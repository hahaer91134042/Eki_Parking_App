package com.hill.devlibs.annotation.parse

import com.hill.devlibs.annotation.PermissionFieldSet
import com.hill.devlibs.model.bean.PermissionSet

/**
 * Created by Hill on 27,09,2019
 */
class PermissionFieldSetParser {

    companion object{
        fun getAllPermission(clazz: Class<*>):ArrayList<PermissionSet>{
            var list=ArrayList<PermissionSet>()
            clazz.fields.forEach {
                try {
                    var set=it.getAnnotation(PermissionFieldSet::class.java)
                    list.add(PermissionSet(set.Name,set.Code))
                }catch (e:Exception){
                }
            }
            return list
        }

        fun getPermission(list:Array<out Enum<*>>):ArrayList<PermissionSet>{
            var pList=ArrayList<PermissionSet>()
            list.forEach {
                try {
                    var clazz=it::class.java
                    var set=clazz.getField(it.name).getAnnotation(PermissionFieldSet::class.java)
                    pList.add(PermissionSet(set.Name,set.Code))
                }catch (e:Exception){

                }
            }
            return pList
        }
    }
}