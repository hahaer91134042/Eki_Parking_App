package com.eki.parking.Controller.process

import android.content.Intent
import com.eki.parking.AppFlag
import com.eki.parking.extension.sqlClean
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.Model.sql.EkiOrder
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.Model.sql.ManagerLvPercent
import kotlinx.coroutines.*

/**
 * Created by Hill on 2019/12/09
 */
class LogoutProcess : BaseProcess(),CoroutineScope by MainScope(){
    override fun run() {
        async(Dispatchers.IO){
            sqlClean<EkiMember>()
            sqlClean<EkiOrder>()
            sqlClean<ManagerLocation>()
            sqlClean<ManagerLvPercent>()
            launch {
                onOver()
                app.sendBroadcast(Intent(AppFlag.OnLogout))
            }
        }

//        isProcessOver=true

    }
}