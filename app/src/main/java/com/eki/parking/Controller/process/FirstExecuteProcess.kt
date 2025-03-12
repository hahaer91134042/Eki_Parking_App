package com.eki.parking.Controller.process

import android.content.Context
import com.eki.parking.Model.sql.BankCode
import com.eki.parking.Model.sql.CarBrand
import com.eki.parking.R
import com.eki.parking.extension.sqlHasData
import com.eki.parking.extension.sqlSaveAsync
import com.eki.parking.Model.sql.CountryCode
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.model.sql.SqlVO
import com.opencsv.CSVReader
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Hill on 2019/12/11
 */
class FirstExecuteProcess(from: Context) : BaseProcess(from),CoroutineScope{

    override fun run() {
        launch {
            if (!sqlHasData<CountryCode>()){
                var countryList=readFile(R.raw.country_code,CountryCode::class.java)
                sqlSaveAsync(countryList)
            }

            if(!sqlHasData<CarBrand>()){
                var brandList=readFile(R.raw.vehicle_series,CarBrand::class.java)
                sqlSaveAsync(brandList)
            }
            if (!sqlHasData<BankCode>()){
                var bankList=readFile(R.raw.bank_code_tw,BankCode::class.java)
                sqlSaveAsync(bankList)
            }
//            isProcessOver=true
            onOver()
        }
    }

    private fun <T:SqlVO<*>> readFile(rawRes: Int,clazz:Class<T>): ArrayList<T> {
        return ArrayList<T>().apply {
            from.notNull { context ->
                var reader=CSVReader(context.resources.openRawResource(rawRes).reader())
                var arr= reader.readAll()
                arr.forEach{data->
                    add(clazz.newInstance().also { it.initFromArray(data) })
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}