package com.eki.parking.Controller.activity

import com.eki.parking.AppConfig
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.Reserva.LocationDetailFrag
import com.eki.parking.Controller.activity.frag.Reserva.SelectDateTimeFrag
import com.eki.parking.Controller.activity.frag.Reserva.SendReservaFrag
import com.eki.parking.Controller.asynctask.task.server.RequestTask
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Model.EnumClass.EkiApi
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.SendIdBody
import com.eki.parking.Model.response.LoadReservaResponse
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.extension.sqlData
import com.eki.parking.extension.sqlSaveOrUpdate
import com.hill.devlibs.extension.getParcel
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.time.ext.format

/**
 * Created by Hill on 30,10,2019
 */
class ReservaActivity: TitleBarActivity() {

    private lateinit var loc:EkiLocation

    private val selectDateTimeFrag=SelectDateTimeFrag().also {
        it.onSelectReserveTime={ orderReservaTime, start, end ->
//            Log.w("OrderReservaTime json ->${orderReservaTime.toJsonStr()}")
            replaceFragment(FragLevelSet(3).setFrag(SendReservaFrag().also {frag->
                frag.setData(orderReservaTime)
                frag.setLocation(loc)
                frag.setReservaTime(start,end)
            }),FragSwitcher.RIGHT_IN)
        }
    }

    override fun initActivityView() {
        loc=intent.getParcel(AppFlag.DATA_FLAG)
        selectDateTimeFrag.setData(loc)

        replaceFragment(FragLevelSet(1)
                .setFrag(LocationDetailFrag().also { frag->
                    frag.setData(loc)
                    frag.onGoReserva={
                        startSelectTimeFrag()
                    }
                }),
                FragSwitcher.NON)

//        loc.printValue()

    }

    private fun startSelectTimeFrag(){
        RequestTask<LoadReservaResponse>(this,
                EkiRequest<SendIdBody>().apply {
                    body= SendIdBody(EkiApi.LoadReservaStatus).also {
                        it.id.add(loc.Id)
                        loc.Info?.SerialNumber.notNull {num->it.serNum.add(num) }
                        sqlData<EkiMember>().notNull { member->it.token=member.token }
                        it.time=DateTime().format(AppConfig.ServerSet.dateTimeFormat)
                    }
                },
                true).setExecuteListener(object : OnResponseListener<LoadReservaResponse> {
            override fun onReTry() {

            }

            override fun onTaskPostExecute(result: LoadReservaResponse) {

                result.notNull {
                    it.info.forEach {data->
                        data.toSql().sqlSaveOrUpdate()
                    }
                    replaceFragment(FragLevelSet(2).setFrag(selectDateTimeFrag),FragSwitcher.RIGHT_IN)
                }

            }

            override fun onFail(errorMsg: String,code:String) {
            }
        }).start()
    }

    override fun setUpResumeComponent() {

    }

    override fun whenBackToFragLv1() {
//        Log.w("$classTag  ---whenBackToFragLv1---")
    }
    override fun toolBarRes(): Int =R.id.toolbar
    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_reserva
    }
//    override fun getTitleBarClass(): ActivityClassEnum =ActivityClassEnum.Reserva
    override fun setUpActivityView(): Int = R.layout.activity_title_bar
}