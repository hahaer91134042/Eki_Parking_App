package com.eki.parking.Model.sql

import com.eki.parking.Controller.tools.EkiEncoder
import com.eki.parking.Model.DTO.AddressInfo
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.impl.IDbQueryArgs
import com.eki.parking.Model.sql.bean.BankDecode
import com.eki.parking.extension.decodeToObj
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.extension.isNull
import com.hill.devlibs.impl.IConvertData
import com.hill.devlibs.impl.ICryptoSet
import com.hill.devlibs.impl.IEncoder
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.util.DbUtil

/**
 * Created by Hill on 2020/04/23
 */
@SqlTableSet(table = "MemberBank")
class MemberBank:SqlVO<MemberBank>(),IDbQueryArgs,ICryptoSet,IConvertData<RequestBody.BankInfo>{

    @SqlColumnSet(key="Id",attr = SqlAttribute.ID,order = 1)
    var Id=0
    @SqlColumnSet(key="Name",attr = SqlAttribute.TEXT,order = 2)
    var Name=""
    @SqlColumnSet(key="Key",attr = SqlAttribute.TEXT,order = 3)
    var Key=""
    @SqlColumnSet(key="Bank",attr = SqlAttribute.TEXT,order = 4)
    var Bank=""
    @SqlColumnSet(key="IsPerson",attr = SqlAttribute.BOOLEAN,order = 5)
    var IsPerson=true
    @SqlColumnSet(key="Address",attr = SqlAttribute.Obj,order = 6)
    var Address=AddressInfo()

//    fun bankDecode():BankDecode?=this.decodeToObj()

    val bankDecode:BankDecode
        get(){
            decode.isNull { decode=decodeToObj() }
            return decode!!
        }

    private var decode:BankDecode?=null
//    fun getDecode(): String {
//        var hash="$Key${AppConfig.privateKey}".toSHA1()
//        var aesCryption= AESCryption().also {
//            it.key=hash.substring(0,16).toASCII().md5Digest()
//            it.hashIV=hash.substring(8,16).toASCII().md5Digest()
//            it.mode= CipherMode.CBC
//        }
//        var result=aesCryption.decodeToString(Bank)
//        Log.d("decode result->$result")
//        return result?:"無法解"
//    }

    override fun clear() {

    }

    override fun queryBuilder(): IQueryArgsBuilder =object : IQueryArgsBuilder() {
        override val select: String
            get() = DbUtil.select("Id")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(Id)
        override val where: String
            get() = DbUtil.where("Id",Id)
    }

    override fun key(): String =Key
    override fun cipher(): String =Bank
    override fun encoder(): IEncoder =EkiEncoder.AES
    override fun toData(): RequestBody.BankInfo =RequestBody.BankInfo().also {
        val decode=bankDecode
        it.name=Name
        it.isPerson=IsPerson
        it.serial=decode.Serial
        it.account=decode.Account
        it.code=decode.Code
        it.sub=decode.Sub
    }

}