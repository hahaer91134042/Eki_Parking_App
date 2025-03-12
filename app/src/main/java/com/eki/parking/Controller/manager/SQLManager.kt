package com.eki.parking.Controller.manager


import android.content.ContentValues
import android.net.Uri
import com.eki.parking.App
import com.eki.parking.Controller.listener.OnSqlAsyncExecute
import com.eki.parking.Controller.listener.OnSqlAsyncLoad
import com.eki.parking.Controller.sql.DBContentProvider
import com.eki.parking.Controller.sql.SqlConfig
import com.eki.parking.Model.impl.IDbQueryArgs
import com.hill.devlibs.annotation.parse.TableParser
import com.hill.devlibs.extension.getValue
import com.hill.devlibs.extension.instanceIs
import com.hill.devlibs.manager.LibSQLManager
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.DbUtil
import kotlinx.coroutines.*

/**
 * Created by Hill on 2018/2/2.
 */

class SQLManager(app: App) : LibSQLManager<DBContentProvider>(app),
                             CoroutineScope by MainScope(){

    // TODO:Edit by Hill 2018/11/19 假如需要監聽DB變動的事件再設定
    enum class NowAction {
        ShopCarNumber
    }


    interface OnActionListener {
        fun onManagerChanged(action: NowAction)
    }

    companion object{
        @JvmStatic
        var actionList = ArrayList<OnActionListener>()
        @JvmStatic
        fun addListener(l: OnActionListener) {
            actionList.add(l)
        }
        @JvmStatic
        fun removeListener(l: OnActionListener) {
            actionList.remove(l)
        }
        @JvmStatic
        fun hasListener(l: OnActionListener): Boolean {
            return actionList.contains(l)
        }
    }


    private fun onAction(action: NowAction) {

        for (listener in actionList) {
            listener.onManagerChanged(action)
        }
    }

    fun <E:SqlVO<*>> deleteFromId(data:E){
        var clazz=data.javaClass
        var fieldPair=TableParser(data).idFieldSet
        sqlDelete(getUri(clazz),where(fieldPair.set.key,fieldPair.field.getValue(data).toString()))
    }
    fun <E:SqlVO<*>> deleteFromArgs(data:E){
        var clazz=data.javaClass
        data.instanceIs<IDbQueryArgs> {
//            var old=getObjListByArgs(clazz,it.queryBuilder())
//            Log.w("delete old->$old")
            sqlDelete(getUri(clazz),it.queryBuilder().select,it.queryBuilder().selectArgs)
        }
    }

    //java兼容
//    fun <E:SqlVO<*>> saveAsync(list: List<E>, clazz: Class<E>)=
//            saveAsync(list,clazz,null)

    @JvmOverloads
    fun <E:SqlVO<*>> saveAsync(list: List<E>, clazz: Class<E>, l:OnSqlAsyncExecute?=null)
            =async(Dispatchers.IO) {
        runCatching {
//            Log.w("--- Save Async start---")
//            for(i in 0..100){ for Test
//                Log.i("Save int ${i}")
//            }
            save(list,clazz)
            launch(Dispatchers.Main){
                Log.d("Sql save async success list size->${list.size}")
                l?.onSuccess()
            }
        }.onFailure {
            it.printStackTrace()
            launch(Dispatchers.Main){
                l?.onFail()
            }
        }
    }

    @JvmOverloads
    fun <E:SqlVO<*>> appendAsync(list: List<E>, l:OnSqlAsyncExecute?=null)
            =async(Dispatchers.IO) {
        runCatching {
//            Log.w("--- Save Async start---")

//            list.forEach {
//                addRow(it)
//            }
            addList(list)
            launch(Dispatchers.Main){
                l?.onSuccess()
            }
        }.onFailure {
            it.printStackTrace()
            launch(Dispatchers.Main){
                l?.onFail()
            }
        }
    }


    fun <E:SqlVO<*>> save(list: List<E>, clazz: Class<E>){
        var set=TableParser(clazz).tableSet
//        Log.w("save list delete all uri->${getUri(clazz)}  isClear->${set.isClear}")
//        Log.i("Save list size->${list.size}")
        //先設定要清掉的表 在批次新增
        if (set.isClear)
            sqlDeleteAll(getUri(clazz))

//        list.forEach {
////            it.printValue()
//            addRow(it)
//        }
        addList(list)
    }

    fun save(data: SqlVO<*>?):Boolean{
        return save(data,TableParser(data?.javaClass).tableSet.isClear)
    }

    //不考慮清掉時用
    private fun addList(data: List<SqlVO<*>>){
        if (data.isNotEmpty()){
            var list=ArrayList<ContentValues>()
            data.forEach { list.add(it.contentValues()) }
            var clazz=data.firstOrNull()?.javaClass
            var uri=getUri(clazz!!)
//            Log.d("--sql addList-- list->$data")
            sqlInsert(uri,list)
        }
    }

    fun save(data: SqlVO<*>?, isClear:Boolean):Boolean{
        return runCatching {
            var clazz=data?.javaClass
//            Log.d("$TAG save data class->$clazz  class name->${clazz?.name}")
//            Log.w(" ::class->${data!!::class.java}")
            var content= data?.contentValues()
            var uri=getUri(clazz!!)
            if (isClear)
                sqlDeleteAll(uri)
            sqlInsert(uri,content)
        }.isSuccess
    }

    fun <VO:SqlVO<*>> getObjDataByArgs(clazz:Class<VO>,args:DbUtil.QueryArgs):VO?{
        try {
            //        var clazz=VO::class.java
            var uri=getUri(clazz)
            var column=SqlConfig.sqlTableList[clazz].parser.tableColumn
            var data=getSqlDataTable(uri,column,args)


            var obj=clazz.newInstance()
            if (obj.initFromRow(data[0]))
                return obj
        }catch (e:Exception){
            printException(e)
        }
        return null
    }

    fun <VO:SqlVO<*>> getObjData(clazz:Class<VO>):VO?{
        try {
            //        var clazz=VO::class.java
            var uri=getUri(clazz)
            var column=SqlConfig.sqlTableList[clazz].parser.tableColumn
            var data=getSqlDataTable(uri,column)

//            data.forEach { dataRow ->
//                Log.w("data row->$dataRow")
//                dataRow.forEach {pair->
//                    Log.i("key->${pair.key} ratio->${pair.ratio}")
//                }
//            }
            var obj=clazz.newInstance()
            if (obj.initFromRow(data.first()))
                return obj
        }catch (e:Exception){
            printException(e)
        }
        return null
    }

    fun <VO:SqlVO<*>> getObjListAsync(clazz:Class<VO>,l:(ArrayList<VO>)->Unit){
        getObjListAsync(clazz,object :OnSqlAsyncLoad<ArrayList<VO>>{
            override fun OnSuccess(data: ArrayList<VO>) =l(data)
        })
    }

    fun <VO:SqlVO<*>> getObjListAsync(clazz:Class<VO>,l:OnSqlAsyncLoad<ArrayList<VO>>)
            =async(Dispatchers.IO){
        runCatching {
            var data=getObjList(clazz)
            launch(Dispatchers.Main){
                l.OnSuccess(data)
            }
        }.onFailure {
            launch(Dispatchers.Main){
                l.OnSuccess(ArrayList())
            }
        }
    }

    fun <VO:SqlVO<*>> getObjListByArgs(clazz:Class<VO>,args:DbUtil.QueryArgs):ArrayList<VO>{
        var list=ArrayList<VO>()
        try {
            var uri=getUri(clazz)
            var column=SqlConfig.sqlTableList[clazz].parser.tableColumn
            var data=getSqlDataTable(uri,column,args)

            data.forEach { row->
                var obj=clazz.newInstance()
                if (obj.initFromRow(row))
                    list.add(obj)
            }

        }catch (e:Exception){
            printException(e)
        }
        return list
    }

    fun <VO:SqlVO<*>> getObjList(clazz:Class<VO>):ArrayList<VO>{
        var list=ArrayList<VO>()
        try {
            var uri=getUri(clazz)
            var column=SqlConfig.sqlTableList[clazz].parser.tableColumn
            var data=getSqlDataTable(uri,column)

            data.forEach { row->
                var obj=clazz.newInstance()
                if (obj.initFromRow(row))
                    list.add(obj)
            }

        }catch (e:Exception){
            printException(e)
        }
        return list
    }

    fun <E:SqlVO<*>> updateByArgs(obj:E){
        runCatching {
            obj.instanceIs<IDbQueryArgs> {
                var clazz=obj.javaClass
                sqlUpdate(getUri(clazz),obj.contentValues(),it.queryBuilder().select,it.queryBuilder().selectArgs)
            }
        }.onFailure {
            Log.e("Sql Update Fail->$it")
        }
    }

    fun <VO:SqlVO<*>> updateById(obj:VO){
        runCatching {
            var clazz=obj.javaClass
            var columnSet=SqlConfig.sqlTableList[clazz].parser.idFieldSet

            var idKey=columnSet.set.key

//        Log.w("update class->$clazz class Fields->${clazz.fields} idKey->$idKey")
//
//        for (field in clazz.declaredFields) {
//            field.isAccessible=true
//            Log.i("file name->${field.name} ratio->${field.get(obj)}")
//        }

            var idValue=clazz.getDeclaredField(columnSet.field.name).let{
                it?.isAccessible=true
                return@let it?.get(obj)
            }
//        Log.w("sql update key->$idKey ratio->$idValue")
            sqlUpdate(getUri(clazz),obj.contentValues(),where(idKey,idValue.toString()))
        }.onFailure {
            Log.e("Sql Update Fail->$it")
        }
    }

    fun <VO:SqlVO<*>> clean(clazz:Class<VO>){
        sqlDeleteAll(getUri(clazz))
    }

    private fun getUri(clazz:Class<*>): Uri? {
        if(SqlConfig.sqlTableList.contains(clazz)) {
            return SqlConfig.sqlTableList[clazz].uri
        }
        return null
    }
    private fun getColumn(clazz:Class<*>): Array<out String>? {
        return SqlConfig.sqlTableList[clazz].parser.tableColumn
    }

    override fun <T : Any?> hasData(clazz: Class<T>): Boolean {
        var uri=getUri(clazz)
        var column=getColumn(clazz)
        return hasDataInSql(uri,column)
    }

    override fun <T : Any?> hasData(clazz: Class<T>,args:DbUtil.QueryArgs): Boolean {
        var uri=getUri(clazz)
        var column=getColumn(clazz)
        return hasDataInSql(uri,column,args.select,args.selectArgs)
    }

    override fun cleanAll() {
        SqlConfig.sqlTableList.forEach {
            sqlDeleteAll(it.uri)
        }
    }

    override fun dbUri(): Uri =Uri.parse("content://${SqlConfig.AUTHORITY}")

}
