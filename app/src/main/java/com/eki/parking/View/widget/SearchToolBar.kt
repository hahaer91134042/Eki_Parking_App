package com.eki.parking.View.widget

import android.content.Context
import android.os.Handler
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.eki.parking.R
import com.eki.parking.View.abs.RelativeCustomView

/**
 * Created by Hill on 22,10,2019
 */
@Deprecated("目前沒用")
class SearchToolBar(context: Context?) : RelativeCustomView(context) {

    var searchView:EkiSearchView=itemView.findViewById(R.id.searchText)
    var cleanBtn:ImageView=itemView.findViewById(R.id.cleanBtn)

    var onStartSearch:((String)->Unit)?=null
    var onSearchTextChange:((String)->Unit)?=null
    var onCleanText:(()->Unit)?=null

    init {
        if (searchView.requestFocus())
            showKeyBoard()

        searchView.listener=object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                onStartSearch?.invoke(query?:"")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                onSearchTextChange?.invoke(newText?:"")
                return true
            }
        }

        cleanBtn.setOnClickListener {
            cleanSearch()
        }
    }

    private fun showKeyBoard() {
        Handler().postDelayed({
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT)
        },500)

//        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }
    fun cleanSearch(){
        searchView.setText("")
        onCleanText?.invoke()
    }
    fun setText(text:String){
        searchView.setText(text)
    }

    override fun setInflatView(): Int = R.layout.item_search_tool_bar

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}