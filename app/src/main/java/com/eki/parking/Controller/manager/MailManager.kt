package com.eki.parking.Controller.manager

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.eki.parking.Controller.impl.IMail

/**
 * Created by Hill on 2020/10/12
 */
class MailManager(context: Context?) : BaseManager(context) {

    var activity:Activity?=null

    fun send(mail: IMail){
        try {
            activity?.startActivity(Intent.createChooser(mail.toIntent(), "Send mail..."))
        } catch (ex: ActivityNotFoundException) {

        }
    }


    private fun IMail.toIntent():Intent=
            Intent(Intent.ACTION_SENDTO).also { intent->
                intent.type = "message/rfc822"
                intent.data = Uri.parse("mailto:")

//                Log.w("mail to->${this.sendMailTo().toTypedArray()}")

                intent.putExtra(Intent.EXTRA_EMAIL, this.sendMailTo().toTypedArray())
                intent.putExtra(Intent.EXTRA_SUBJECT, this.mailSubject())
                intent.putExtra(Intent.EXTRA_TEXT, this.contentText())
            }


}