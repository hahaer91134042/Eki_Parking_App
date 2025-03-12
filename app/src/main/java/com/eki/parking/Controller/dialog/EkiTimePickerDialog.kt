package com.eki.parking.Controller.dialog

import android.graphics.Color
import com.appeaser.sublimepickerlibrary.SublimePicker
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate
import com.appeaser.sublimepickerlibrary.helpers.SublimeListenerAdapter
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker
import com.eki.parking.Controller.dialog.abs.BaseFragDialog
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.R
import com.hill.devlibs.EnumClass.DialogOption
import com.hill.devlibs.extension.getColor
import com.hill.devlibs.extension.getColorList
import com.hill.devlibs.extension.notNull


/**
 * Created by Hill on 04,10,2019
 */
class EkiTimePickerDialog : BaseFragDialog<DialogChildFrag<*>>() {


    private var callBack: ((Int, Int) -> Unit)? = null

    override fun setUpDialogFeature(): DialogSetting = DialogSetting(
        DialogOption.NoTitle,
        DialogOption.CantCancelable,
        DialogOption.Colorless,
        DialogOption.FullDialogWidth
    )

    var adapter = object : SublimeListenerAdapter() {
        override fun onDateTimeRecurrenceSet(
            sublimeMaterialPicker: SublimePicker?,
            selectedDate: SelectedDate?,
            hourOfDay: Int,
            minute: Int,
            recurrenceOption: SublimeRecurrencePicker.RecurrenceOption?,
            recurrenceRule: String?
        ) {
            callBack?.invoke(hourOfDay, minute)
            dismiss()
        }

        override fun onCancelled() {
            dismiss()
        }
    }

    fun onTimeSelect(l: (Int, Int) -> Unit) {
        callBack = fun(hourOfDay: Int, minute: Int) {
            l(hourOfDay, minute)
        }
    }

    override fun setUpParentDialogComponent() {
        val sublime_picker = view?.findViewById<SublimePicker>(R.id.sublime_picker)
        sublime_picker?.let {
            var options = SublimeOptions().apply {
                pickerToShow = SublimeOptions.Picker.TIME_PICKER
            }
            options.setDisplayOptions(SublimeOptions.ACTIVATE_TIME_PICKER)

            sublime_picker.initializePicker(options, adapter)
            sublime_picker.setPostiveBtnTextColor(getColor(R.color.Eki_orange_2))
            sublime_picker.setNegativeBtnTextColor(getColor(R.color.text_color_1))
            sublime_picker.timePicker.apply {
                headerView.setBackgroundColor(Color.WHITE)
                setHeaderTexColor(getColorList(R.color.time_picker_header_color))
                setPickerSelectorColor(getColor(R.color.Eki_orange_2)!!)
                getColor(R.color.light_gray4).notNull { setPickerNumBackgroundColor(it) }
            }
        }
    }

    override fun initDialogViewFrag(): DialogChildFrag<*>? = null

    override fun setParentViewInflate(): Int = R.layout.dialog_time_picker

}