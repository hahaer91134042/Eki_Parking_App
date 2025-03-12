package com.eki.parking.Model.EnumClass

import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.R
import com.hill.devlibs.impl.IThemeColor

/**
 * Created by Hill on 2020/10/29
 * 該死的主題色
 * 車主:橘色
 * 地主:綠色
 */
enum class PPYPTheme:IThemeColor{

    CarUser {
        override fun colorRes(): Int = R.color.Eki_orange_4
        override fun styleRes(): Int = R.style.AppThemeOrange
        override fun dialogTitle(): IDialogFeatureSet.ITitleBarSet =IDialogFeatureSet.OrangeDialogTitle()
    },
    Manager {
        override fun colorRes(): Int =R.color.Eki_green_2
        override fun styleRes(): Int = R.style.AppThemeGreen
        override fun dialogTitle(): IDialogFeatureSet.ITitleBarSet =IDialogFeatureSet.GreenDialogTitle()
    };

    abstract fun dialogTitle():IDialogFeatureSet.ITitleBarSet
}