package com.eki.parking.extension

import androidx.fragment.app.FragmentManager
import com.eki.parking.Controller.activity.intent.CheckOutProcessIntent
import com.eki.parking.Controller.dialog.FragContainerDialog
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.Model.sql.EkiOrder
import com.hill.devlibs.extension.notNull

/**
 * Created by Hill on 2019/12/31
 */
inline fun <T:IDialogFeatureSet<*>> SearchFrag.showFragDialog(set:T) = set.show(childFragmentManager)

fun <T:IDialogFeatureSet<*>> T.show(manager:FragmentManager){
    FragContainerDialog().also {
        it.setContent(this)
    }.show(manager,frag.TAG)
}

inline fun BaseFragment<*>.checkOutOrder(order: EkiOrder){
    activity.notNull { startActivitySwitchAnim(CheckOutProcessIntent(it,order),false) }
}
