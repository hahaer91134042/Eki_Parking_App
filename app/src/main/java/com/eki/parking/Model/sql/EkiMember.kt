package com.eki.parking.Model.sql

import com.eki.parking.Model.DTO.AddressInfo
import com.eki.parking.Model.DTO.PayTokenLifeInfo
import com.eki.parking.Model.DTO.RequestBody
import com.eki.parking.Model.DTO.VehicleInfo
import com.eki.parking.Model.impl.IDbQueryArgs
import com.eki.parking.Model.request.body.MemberEditBody
import com.eki.parking.Model.request.body.RegisterBody
import com.eki.parking.Model.response.LoginResponse
import com.eki.parking.Model.sql.bean.MemberInfo
import com.hill.devlibs.annotation.SqlColumnSet
import com.hill.devlibs.annotation.SqlTableSet
import com.hill.devlibs.annotation.feature.SqlAttribute
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IConvertData
import com.hill.devlibs.impl.IImgUrl
import com.hill.devlibs.impl.IQueryArgsBuilder
import com.hill.devlibs.model.sql.SqlVO
import com.hill.devlibs.tools.ContentPrinter
import com.hill.devlibs.util.DbUtil

/**
 * Created by Hill on 2019/6/12
 */
@SqlTableSet(table = "EkiMember")
class EkiMember() : SqlVO<EkiMember>(),
                    IImgUrl,
                    IDbQueryArgs,
                    IConvertData<MemberEditBody>{

    constructor(response: LoginResponse?):this(){
        token=response?.info?.token?:""
        mail=response?.info?.mail?:""
        phoneNum=response?.info?.phoneNum?:""
        beManager=response?.info?.beManager?:false
        referrer=response?.info?.referrer?:""
        Lv=response?.info?.Lv?:0
        address=response?.info?.address
        info=response?.info?.member
        response?.info?.vehicleList.notNull { vehicle=it }
        payTokenLife = response?.info?.payTokenLife
    }

    //剛註冊完Lv一定是0
    constructor(body: RegisterBody):this(){
        mail=body.mail
        phoneNum=body.phone
        beManager=body.beManager
        address= AddressInfo(body.address)
        info= MemberInfo(body.info)
    }

    override fun queryBuilder(): IQueryArgsBuilder =object : IQueryArgsBuilder() {
        override val select: String
            get() = DbUtil.select("id")
        override val selectArgs: Array<out String>
            get() = DbUtil.selectArgs(id)
        override val where: String
            get() = DbUtil.where("id",id)
    }

    @SqlColumnSet(key="id",attr = SqlAttribute.ID,order = 1)
    var id=0

    @SqlColumnSet(key = "token",attr = SqlAttribute.TEXT,order = 2)
    var token=""

    @SqlColumnSet(key = "mail",attr = SqlAttribute.TEXT,order = 3)
    var mail=""

    @SqlColumnSet(key="phoneNum",attr = SqlAttribute.TEXT,order = 4)
    var phoneNum=""

    @SqlColumnSet(key = "beManager",attr = SqlAttribute.BOOLEAN,order = 5)
    var beManager=false

    @SqlColumnSet(key = "referrer",attr = SqlAttribute.TEXT,order = 6)
    var referrer=""

    @SqlColumnSet(key = "Lv",attr = SqlAttribute.INT,order = 7)
    var Lv=0

    @SqlColumnSet(key = "address",attr = SqlAttribute.Obj)
    var address: AddressInfo?= AddressInfo()

    @SqlColumnSet(key = "info",attr = SqlAttribute.Obj)
    var info: MemberInfo?= MemberInfo()

    @SqlColumnSet(key = "vehicle",attr = SqlAttribute.Array)
    var vehicle:ArrayList<VehicleInfo> =ArrayList()

    @SqlColumnSet(key = "payTokenLife",attr = SqlAttribute.Obj)
    var payTokenLife: PayTokenLifeInfo? = null

    override fun clear() {

    }

    override fun printValue() {
        printContentValue(ContentPrinter()
                .setKeys("token","mail","phoneNum")
                .setValues(token,mail,phoneNum))
        address?.printValue()
        info?.printValue()
    }

    override fun imgUrl(): String =info?.IconImg?:""

    override fun toData(): MemberEditBody =
            MemberEditBody().also { m->

                info.notNull { info->
                    m.info=RequestBody.MemberInfo().apply {
                        firstName=info.FirstName
                        lastName=info.LastName
                        nickName=info.NickName
                        countryCode=info.CountryCode
                        phone=info.PhoneNum
                    }
                }
                address.notNull { addr->
                    m.address=RequestBody.Address().apply {
                        country=addr.Country
                        state=addr.State
                        city=addr.City
                        detail=addr.Detail
                        zip=addr.ZipCode
                    }
                }
            }
}