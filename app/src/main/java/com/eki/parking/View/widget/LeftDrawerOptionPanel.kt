package com.eki.parking.View.widget

import android.content.Context

import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.eki.parking.Model.EnumClass.MenuOptionEnum
import com.eki.parking.R
import com.eki.parking.Controller.listener.OnMenuSelectListener
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.Controller.util.ScreenUtils
import com.hill.devlibs.listener.ItemClickListener
import com.hill.devlibs.tools.Log
import com.hill.devlibs.util.StringUtil
import java.util.logging.Handler


class LeftDrawerOptionPanel (context: Context, attrs: AttributeSet? = null) : BaseRecycleView(context, attrs),ItemClickListener{

    var runnable : Runnable? = null;
    var handler : Handler? = null;
    var listener: OnMenuSelectListener?=null
        private set

    private val memberOptionList:ArrayList<MenuOptionEnum> by lazy {
        return@lazy ArrayList<MenuOptionEnum>().apply {
            add(MenuOptionEnum.CarSetting)
//            add(MenuOptionEnum.Calendar)
            add(MenuOptionEnum.ConsumptionHistory)
            add(MenuOptionEnum.MemberProblemResponse)
            add(MenuOptionEnum.ParkingRules)
            add(MenuOptionEnum.Logout)
        }
    }

    private val managerOptionList:ArrayList<MenuOptionEnum> by lazy {
        return@lazy ArrayList<MenuOptionEnum>().apply {
            add(MenuOptionEnum.ParkingSiteSetting)
            add(MenuOptionEnum.ReservationStatus)
            add(MenuOptionEnum.SiteOpenTime)
            add(MenuOptionEnum.BillingOverView)
            add(MenuOptionEnum.ManagerProblemResponse)
            add(MenuOptionEnum.LandOwnerRule)
            add(MenuOptionEnum.Logout)
        }
    }

    private val anonymousOptionList:ArrayList<MenuOptionEnum> by lazy {
        return@lazy ArrayList<MenuOptionEnum>().apply {
            add(MenuOptionEnum.ProblemResponse)
            add(MenuOptionEnum.ParkingRules)
            add(MenuOptionEnum.LoginOrRegister)
        }
    }

    private var optionAdaptor:OptionAdapter?=null

    private var optionList=ArrayList<MenuOptionEnum>()

    init {

        isNestedScrollingEnabled=true
        layoutManager = LinearLayoutManager(context)
        //addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.DividerColor.White))
    }

    fun anonymousOption(){
        optionList.clear()
        optionList.addAll(anonymousOptionList)
        loadList(optionList)
    }

    fun memberOption(){
        optionList.clear()
        optionList.addAll(memberOptionList)
        loadList(optionList)
    }

    fun managerOption(){
        optionList.clear()
        optionList.addAll(managerOptionList)
        loadList(optionList)
    }


    private fun loadList(optionList: ArrayList<MenuOptionEnum>) {
        removeAllViews()
        optionAdaptor=OptionAdapter(context,optionList)
        optionAdaptor?.setItemListClickListener(this)
        adapter=optionAdaptor
//        optionAdaptor?.refresh(managerOptionList)
    }

    override fun onItemClick(which: Int) {
        listener?.onMenuSelect(optionList[which])
//        listener?.onMenuSelect(when(optionType){
//            OptionType.User->memberOptionList[which]
//            OptionType.LandLoad->managerOptionList[which]
//        })
    }


    fun setMenuListener(l:OnMenuSelectListener): LeftDrawerOptionPanel {
        listener=l
        return this
    }

    private inner class OptionAdapter(context: Context,var optionList: ArrayList<MenuOptionEnum>) : BaseAdapter<OptionItem>(context) {


        init {
            setItemListClickListener {

                Log.d("MenuOption->${optionList[it]}")

            }

        }



        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionItem {
            val view = inflater.inflate(R.layout.item_textview, parent, false)
            return OptionItem(view).also { it.init(width) }
        }

        override fun onBindViewHolder(item: OptionItem, position: Int) {
            super.onBindViewHolder(item, position)
            item?.refresh(optionList[position])
        }

        override fun getItemCount(): Int {
            return optionList.size
        }

    }

    private class OptionItem(itemView: View) : ItemLayout<MenuOptionEnum>(itemView) {
        val textView: TextView by lazy{
            var view= itemView.findViewById(R.id.textView) as TextView
//            view.layoutParams.width=ViewGroup.LayoutParams.WRAP_CONTENT
            val pad = ScreenUtils.dpToPx(2.5f)
            view.setPadding(pad*15, pad*10, pad*2, pad)
            view.compoundDrawablePadding=pad*10
            view.textSize = ScreenUtils.dpToPx(8f).toFloat()
            view.setTextColor(getColor(R.color.text_color_1))
            view.setOnClickListener {
                itemClick()
            }
            return@lazy view
        }
//        val icon:ImageView by lazy {
//            var view=itemView.findViewById<ImageView>(R.id.headerIcon)
//            return@lazy view
//        }

        override fun init(width: Int) {
            super.init(width)

        }

        override fun refresh(data: MenuOptionEnum?) {
            super.refresh(data)
            StringUtil.getImgStringBuilder()
                    .setText(getString(data?.optionRes!!))
                    .setIcon(data?.iconRes!!)
                    .into(textView)
//            icon.setImageResource(data?.iconRes)
        }
    }

}

