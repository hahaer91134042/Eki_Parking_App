package com.eki.parking.Controller.process

import android.content.Context
import android.content.Intent
import com.eki.parking.AppFlag
import com.eki.parking.Controller.listener.OnResponseListener
import com.eki.parking.Controller.tools.ProcessExecutor
import com.eki.parking.Model.request.EkiRequest
import com.eki.parking.Model.request.body.LoginBody
import com.eki.parking.Model.response.LoginResponse
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.extension.sendRequest
import com.eki.parking.extension.showProgress
import com.eki.parking.extension.sqlSave
import com.hill.devlibs.EnumClass.ProgressMode
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.time.DateTime

/**
 * Created by Hill on 2019/12/09
 */
abstract class LoginProcess(from: Context?) :
    BaseProcess(from),
    BaseProcess.ISetResponseListener<OnResponseListener<EkiMember>>,
    BaseProcess.ISetRequestBody<LoginBody> {

    override val request: EkiRequest<LoginBody>
        get() = EkiRequest<LoginBody>().also { it.body = body }

    override fun run() {
        from.notNull { context ->
            val progress = context.showProgress(ProgressMode.PROCESSING_MODE)

            request.sendRequest(context, false, object : OnResponseListener<LoginResponse> {
                override fun onReTry() {
                    progress.notNull { it.dismiss() }
                    onResponse?.onReTry()
                }

                override fun onFail(errorMsg: String, code: String) {
                    progress.notNull { it.dismiss() }
                    onResponse?.onFail(errorMsg, code)
                }

                override fun onTaskPostExecute(result: LoginResponse) {
                    val member = EkiMember(result)
                    sqlSave(member)
                    val pExecute = ProcessExecutor()
                    pExecute.add(object : LoadOrderProcess(context) {})

                    //下載地主車位
                    var dateTime = DateTime()
//                    dateTime = DateTime.parse(
//                        setDateTime(
//                            dateTime.year,
//                            dateTime.month - 1,
//                            dateTime.day,
//                            dateTime.hour,
//                            dateTime.min,
//                            dateTime.sec,
//                            "-",
//                            ":"
//                        ), "yyyy-MM-dd HH:mm:ss"
//                    )

                    if (member.beManager) {
                        pExecute.add(object : ManagerLocationProcess(context, dateTime, true) {})
                        pExecute.add(ManagerLvProcess(context))
                    }

                    pExecute.setOnAllOverBack {
                        progress.dismiss()
                        onOver()
                        from?.sendBroadcast(Intent(AppFlag.OnLogin))
                        this@LoginProcess.onResponse?.onTaskPostExecute(member)
                    }

                    pExecute.run()
                }
            })
        }
    }
}