package com.eki.parking.View.widget

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.eki.parking.R
import com.eki.parking.View.abs.ConstrainCustomView
import com.eki.parking.View.libs.StateButton
import com.eki.parking.extension.pxToDp

/**
 * Created by Hill on 25,10,2019
 */
class SearchToolBarMain(context: Context?) : ConstrainCustomView(context) {

    var searchTex: TextView = findViewById(R.id.searchText)
    var startSearchBtn:StateButton = findViewById(R.id.searchBtn)
    var cleanSearchBtn:StateButton = findViewById(R.id.cleanBtn)

    init {
        startSearchBtn.setBtnIcon(R.drawable.icon_search)
            .setIconPosition(StateButton.IconPosition.Center, pxToDp(2))
            .setIconScale(0.7f)

        cleanSearchBtn.setBtnIcon(R.drawable.icon_close_gray)
            .setIconPosition(StateButton.IconPosition.Center, pxToDp(2))
            .setIconScale(0.7f)

    }

    override fun setInflatView(): Int = R.layout.item_search_tool_bar_2

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null
}