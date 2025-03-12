package com.eki.parking.Controller.activity.frag.BillingOverview

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.eki.parking.R

class ActualIncomeSetAlertDialog {

    fun setAlertDialogBuilder(context: Context?): AlertDialog.Builder{

        val builder = AlertDialog.Builder(context)

        builder.setPositiveButton(context?.getString(R.string.Determine)) { dialog, _ ->

            dialog.dismiss()

        }

        builder.setNegativeButton(context?.getString(R.string.cancel)) { dialog, _ ->

            dialog.cancel()
            dialog.dismiss()

        }

        return builder

    }

    fun setAlertDialog(dialogView: View, builder: AlertDialog.Builder) {

        val dialog: AlertDialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setView(dialogView)
        dialog.show()
        dialog.setOnDismissListener {
            (dialogView.parent as ViewGroup).removeView(dialogView)
        }

    }

}