package com.eki.parking.Controller.activity.frag.Reserva.adaptor

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.eki.parking.Model.DTO.VehicleInfo
import com.eki.parking.R
import com.eki.parking.View.ViewType
import com.eki.parking.View.recycleview.adapter.ViewTypeAdaptor
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.extension.dimen
import com.eki.parking.extension.dpToPx
import com.eki.parking.extension.drawable
import com.hill.devlibs.extension.cleanTex
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.recycleview.model.RecycleViewModel
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/01/15
 */
class VehicleSelectAdaptor(context: Context?,private val list:ArrayList<VehicleInfo>) :
        ViewTypeAdaptor<VehicleInfo>(context) {

    var onNumberChange:((String)->Unit)?=null

    override val modelList: ModelList<VehicleInfo>
        get() = ModelList<VehicleInfo>().apply {
                    append(RecycleViewModel(ViewType.edit))
                    list.forEach {
                        append(RecycleViewModel(ViewType.item, it))
                    }
                }


    override val viewSets: SetList<VehicleInfo>
        get() = SetList(object :ItemTypeSet<VehicleInfo>{
            override val viewType: Int
                get() = ViewType.item
            override fun itemBack(parent: ViewGroup): ItemLayout<VehicleInfo> =
                    VehicleItem(getItemView(R.layout.item_textview, parent))
                            .also { it.init(dpToPx(50f)) }
        },object :ItemTypeSet<VehicleInfo>{
            override val viewType: Int
                get() = ViewType.edit
            override fun itemBack(parent: ViewGroup): ItemLayout<VehicleInfo> =
                    EditItem(getItemView(R.layout.item_edittext, parent))
                            .also { it.init(dpToPx(50f))
                                it.editText.addTextChangedListener(textWatch)}
        })



    init {
        setItemListClickListener {position->

            itemList.forEach { it.itemEvent?.itemNoSelect() }
            var selectEvent=itemList[position].itemEvent
            selectEvent?.onItemSelect()
            Log.d("position->$position  carNumber->${selectEvent?.value()}")
            onNumberChange?.invoke(selectEvent?.value() as String)
        }
    }
    private val textWatch=object :TextWatcher{
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onNumberChange?.invoke(s.toString().cleanTex)
        }
    }


    class EditItem(itemView: View) : ItemLayout<VehicleInfo>(itemView),
                                     ItemLayout.ItemEvent<String> {
        val editText:EditText=itemView.findViewById(R.id.editText)
        private val parentView:RelativeLayout=itemView.findViewById(R.id.parentView)
        override fun init(lenght: Int) {
            super.init(lenght)
            parentView.layoutParams= RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    lenght)
//            parentView.setOnClickListener { itemClick() }
            editText.layoutParams=RelativeLayout.LayoutParams(dpToPx(150f),
                    RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }

            editText.hint=getString(R.string.Please_enter_the_license_plate_number)
            editText.setSingleLine(true)
            editText.gravity=Gravity.CENTER_HORIZONTAL
//            itemView.background= drawable(R.drawable.shape_select_frame)
            editText.setOnFocusChangeListener { v, hasFocus ->
                Log.w("hasFocuse->$hasFocus")
                if (hasFocus)
                    itemClick()
            }
            parentView.setOnClickListener { itemClick() }
        }

        override fun refresh(data: VehicleInfo?) {
            super.refresh(data)
        }

        override fun onItemSelect() {
            parentView.background= drawable(R.drawable.shape_select_frame)
//            editText.isFocusable=true

        }

        override fun itemNoSelect() {
            parentView.background=null

        }

        override fun value(): String =editText.text.toString().cleanTex

    }

    class VehicleItem(itemView: View) : ItemLayout<VehicleInfo>(itemView),
                                        ItemLayout.ItemEvent<String> {
        private var textView:TextView=itemView.findViewById(R.id.textView)

        override fun init(lenght: Int) {
            super.init(lenght)
            textView.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    lenght)
            textView.setTextColor(getColor(R.color.text_color_1))
            textView.textSize= dimen(R.dimen.text_size_6)
            textView.gravity=Gravity.CENTER
            textView.setOnClickListener { itemClick() }
            textView.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus)
                    itemClick()
            }
            //要開了才能正確取消調第一個(EditItem)edittext的focuse
            textView.isFocusable = true
            textView.isFocusableInTouchMode=true
        }

        override fun refresh(data: VehicleInfo?) {
            super.refresh(data)
            data.notNull { info->
                textView.text=info.Number
            }
        }

        override fun value(): String =itemData.Number
        override fun itemNoSelect() {
            textView.background=null
        }
        override fun onItemSelect() {
            textView.background= drawable(R.drawable.shape_select_frame)

        }
    }





//    private class VehicleModel(viewType: Int, data: VehicleInfo?) : RecycleViewModel<VehicleInfo?>(viewType, data)
}