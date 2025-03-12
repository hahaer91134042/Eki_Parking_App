package com.eki.parking.extension

import com.eki.parking.App
import com.eki.parking.Model.impl.IDbQueryArgs
import com.eki.parking.Controller.listener.OnSqlAsyncExecute
import com.hill.devlibs.extension.instanceIs
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2019/11/22
 */
//這是再不清除舊有的data再新增row
inline fun SqlVO<*>.sqlAppend():Boolean=App.getInstance().sqlManager.save(this,false)
inline fun <T:SqlVO<*>> sqlSave(data: T?): Boolean = App.getInstance().sqlManager.save(data)
inline fun <reified T:SqlVO<*>> sqlSaveAsync(list:ArrayList<T>,l: OnSqlAsyncExecute?=null)= App.getInstance().sqlManager.saveAsync(list,T::class.java,l)
inline fun <reified T:SqlVO<*>> sqlAppendAsync(list: ArrayList<T>,l:OnSqlAsyncExecute?=null)=App.getInstance().sqlManager.appendAsync(list,l)
inline fun <T:SqlVO<*>> sqlUpdate(data: T?)=data.notNull { App.getInstance().sqlManager.updateById(it) }
inline fun <reified T:SqlVO<*>> sqlData():T?=App.getInstance().sqlManager.getObjData(T::class.java)
inline fun <reified T:SqlVO<*>> sqlDataList():ArrayList<T> =App.getInstance().sqlManager.getObjList(T::class.java)
inline fun <reified T:SqlVO<*>> sqlDataListAsync(crossinline back:(ArrayList<T>)->Unit)=App.getInstance().sqlManager.getObjListAsync(T::class.java){back(it)}
inline fun <reified T:SqlVO<*>> sqlDataFromArgs(data:T):T{
    data.instanceIs<IDbQueryArgs> {
        var sqlManager=App.getInstance().sqlManager
        var v=sqlManager.getObjDataByArgs(T::class.java,it.queryBuilder().build())
        v.notNull { v1-> return v1 }
    }
    return T::class.java.newInstance()
}
inline fun SqlVO<*>.sqlSaveOrUpdate(){
    this.instanceIs<IDbQueryArgs> {
        Log.d("SqlVO<*>.sqlSaveOrUpdate ->${this.javaClass.simpleName}")
        var sqlmanager=App.getInstance().sqlManager

        if (sqlmanager.hasData(this.javaClass,it.queryBuilder().build())){
//            Log.w("Sql hasData->true")
            sqlmanager.updateByArgs(this)
        }else{
//            Log.i("Sql hasData->false")
            sqlmanager.save(this)
        }

    }
}
inline fun SqlVO<*>.hasData():Boolean{
    this.instanceIs<IDbQueryArgs> {
        Log.d("SqlVO<*>.sqlSaveOrUpdate ->${this.javaClass.simpleName}")
        var sqlmanager=App.getInstance().sqlManager
        return sqlmanager.hasData(this.javaClass,it.queryBuilder().build())
    }
    throw IllegalArgumentException("SqlVO must implement interface ${IDbQueryArgs::class.java.name}")
}

inline fun <reified T: SqlVO<*>> sqlHasData():Boolean = App.getInstance().sqlManager.hasData(T::class.java)
inline fun <reified T:SqlVO<*>> sqlClean()=App.getInstance().sqlManager.clean(T::class.java)
inline fun SqlVO<*>.sqlDelete()=App.getInstance().sqlManager.deleteFromArgs(this)
inline fun SqlVO<*>.sqlDeleteById()=App.getInstance().sqlManager.deleteFromId(this)