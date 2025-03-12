package com.eki.parking.Model.EnumClass

import androidx.annotation.StringRes
import com.eki.parking.R

/**
 * Created by Hill on 2019/6/19
 */
enum class CountryEnum(var conutryCode:String, @StringRes var lan:Int, @StringRes var region:Int) {
    US("1", R.string.lan,R.string.United_States),
    TW("886",R.string.lan,R.string.Taiwan_area),
    JP("81",R.string.lan,R.string.Japan_area),
    CN("86",R.string.lan,R.string.China_area)
}