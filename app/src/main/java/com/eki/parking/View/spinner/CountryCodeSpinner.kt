package com.eki.parking.View.spinner

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.widget.AppCompatSpinner
import com.eki.parking.Controller.listener.OnSpinnerSelectListener
import com.eki.parking.Model.EnumClass.CountryEnum
import com.eki.parking.R

/**
 * Created by Hill on 2019/6/24
 */
class CountryCodeSpinner(context: Context, attrs: AttributeSet?) : AppCompatSpinner(context, attrs), AdapterView.OnItemSelectedListener {


    private var countryList = ArrayList<CountryEnum>().apply {
        add(CountryEnum.US)
        add(CountryEnum.TW)
        add(CountryEnum.CN)
        add(CountryEnum.JP)
        this.sortWith(Comparator { o1, o2 ->
            return@Comparator if (o1.conutryCode.toInt() < o2.conutryCode.toInt()) -1 else 1
        })
    }

    private var codeList = ArrayList<String>().apply {
        countryList.forEach {
            add("+${it.conutryCode}")
        }
    }

    var listener: OnSpinnerSelectListener<String>? = null

    var adapterView: AdapterView<*>?=null

    init {

        setBackgroundResource(android.R.color.transparent)

        var texSize=context?.resources?.getDimension(R.dimen.text_size_5)

        val optionAdaptor = OptionAdapter(context, codeList, OptionViewEnum.Default).apply {
            this.textSize=texSize!!
        }

        optionAdaptor.setTextGravity(SpinnerTextGravity.Left)
        adapter = optionAdaptor
        onItemSelectedListener = this

    }

    fun selectCode(code:String){
        var position=0
        for (i in 0 until  countryList.size){
            if (countryList[i].conutryCode==code)
                position=i
        }

        setSelection(position)
        //adapterView?.setSelection(position)

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        adapterView=parent
        listener?.onItemSelect(position, countryList[position].conutryCode)

    }

}