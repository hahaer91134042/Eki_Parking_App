package com.eki.parking.extension

import com.shawnlin.numberpicker.NumberPicker

/**
 * Created by Hill on 05,11,2019
 */

fun NumberPicker.setList(list:List<String>) {
    val array = list.toTypedArray()
    minValue = 0
    maxValue = array.size - 1
    displayedValues = array
    value = 0
}