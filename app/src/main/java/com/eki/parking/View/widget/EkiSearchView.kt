package com.eki.parking.View.widget

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import com.eki.parking.R
import com.eki.parking.extension.color
import com.eki.parking.extension.dimen
import com.hill.devlibs.util.StringUtil
import com.hill.devlibs.widget.AutoSizeEditText

/**
 * Created by Hill on 22,10,2019
 */
class EkiSearchView(context: Context?, attrs: AttributeSet?) : AutoSizeEditText(context, attrs) {


    var listener:SearchView.OnQueryTextListener?=null
    private var searchStr=""

    init {
//        setBackgroundResource(R.drawable.stroke_round_corner_dark_gray1)
        setBackgroundDrawable(null)

        gravity = Gravity.CENTER_VERTICAL
        setTextColor(color(R.color.text_color_1))
        textSize = dimen(R.dimen.text_size_5)
        setHintTextColor(Color.GRAY)

        //開啟鍵盤的送出button
        imeOptions=EditorInfo.IME_ACTION_SEARCH
        //只開單行
        setSingleLine(true)

        StringUtil.getImgStringBuilder()
                .setIcon(R.drawable.icon_search_orange2)
                .setHint(context?.getString(R.string.Search_Address))
                .into(this)

        addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
//                Log.i("afterTextChanged text->$s")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                Log.w("beforeTextChanged text->$s  start->$start count->$count after->$after")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                Log.d("onTextChanged text->$s  start->$start before->$before count->$count")
                searchStr=s.toString()
                listener?.onQueryTextChange(searchStr)
            }
        })

        setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                Log.i("Click Action Done")

                listener?.onQueryTextSubmit(searchStr)
                true
            }
            false
        }
    }

}