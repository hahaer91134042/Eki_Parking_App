package com.eki.parking.Controller.activity.frag.SiteOpen.child

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IAppTheme
import com.eki.parking.Controller.process.AddOpenSetProcess
import com.eki.parking.Controller.tools.ProcessExecutor
import com.eki.parking.Model.DTO.OpenSet
import com.eki.parking.Model.EnumClass.PPYPTheme
import com.eki.parking.Model.request.body.AddOpenSetBody
import com.eki.parking.Model.sql.ManagerLocation
import com.eki.parking.R
import com.eki.parking.View.ViewType
import com.eki.parking.View.recycleview.BaseRecycleView
import com.eki.parking.View.recycleview.adapter.BaseAdapter
import com.eki.parking.View.recycleview.adapter.ViewTypeAdaptor
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.View.widget.AutoLoadImgView
import com.eki.parking.databinding.FragCopyLocOpenBinding
import com.eki.parking.extension.*
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData
import com.hill.devlibs.recycleview.model.RecycleViewModel
import com.hill.devlibs.time.DateTime
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2022/01/04
 */
class CopyLocOpenFrag:DialogChildFrag<CopyLocOpenFrag>(),
                      ISetData<List<OpenSet>>,
                      IAppTheme,IFragViewBinding {

    private lateinit var binding: FragCopyLocOpenBinding
    private lateinit var copyFrom:List<OpenSet>
    private lateinit var locList:ArrayList<ManagerLocation>

    private var selectLoc:List<ManagerLocation>?=null

    var onCopyAddFinish={

    }

    override fun initFragView() {
//        Log.d("open tag->$TAG")

        var progress = context?.showProgress()
        sqlDataListAsync<ManagerLocation> {
            progress?.dismiss()
            locList=it
            startSiteView()
        }

        binding.determinBtn.setOnClickListener {
            startAddOpenProcess()
        }
    }

    private fun startAddOpenProcess() {
        var now=DateTime.now()
        var pList=selectLoc?.map { loc->
            AddOpenProcess(AddOpenSetBody().apply {
                id=loc?.Id
                openSet.addAll(copyFrom.map { set->set.toData() })
                time=now.toString()
            }){
                //需要的話再使用
                loc.Config?.OpenSet?.apply {
                    addAll(copyFrom)
                }
                loc.sqlSaveOrUpdate()
                onCopyAddFinish()
            }
        }

        var progress=context?.showProgress()
        ProcessExecutor(pList?:listOf()).apply {
            setOnAllOverBack {
                progress?.dismiss()
                showToast(string(R.string.Opening_hours_have_been_added))
                dissmissDialog()
            }
        }.run()

    }

    private fun startSiteView() {
        binding.recycleView.setBackgroundColor(color(R.color.color_white))
        binding.recycleView.useSimpleDivider()

        binding.recycleView.adapter=CopySelectListAdaptor()
    }

    private var onSelectLoc:(List<ManagerLocation>)->Unit={list->
        binding.determinBtn.isEnabled = list.isNotEmpty()
        Log.w("select loc size->${list.size}")
//        list.printList()
        selectLoc=list
    }

    private inner class AddOpenProcess(val set: AddOpenSetBody,back:()->Unit) : AddOpenSetProcess(context){
        override val body: AddOpenSetBody
            get() = set
        init {
            onEditSuccess=back
            onFail={
                showToast("地點:${set.serNum} 新增時間錯誤")
            }
        }
    }

    private inner class CopySelectListAdaptor : ViewTypeAdaptor<List<ManagerLocation>>(context) {
        private var now=DateTime.now()
        override val modelList: ModelList<List<ManagerLocation>>
            get() = ModelList(
                    RecycleViewModel(ViewType.edit,locList.filter {
                        it.Config?.OpenSet?.filter { set->set.startDateTime().date>=now.date }.isNullOrEmpty()
                    }),
                    RecycleViewModel(ViewType.nothing,locList.filter {
                        !it.Config?.OpenSet?.filter { set->set.startDateTime().date>=now.date }.isNullOrEmpty()
                    })
            )
        override val viewSets: SetList<List<ManagerLocation>>
            get() = SetList(object :ItemTypeSet<List<ManagerLocation>>{
                override val viewType: Int
                    get() = ViewType.edit

                override fun itemBack(parent: ViewGroup): ItemLayout<List<ManagerLocation>>
                =SelectCopyListItem(getItemView(R.layout.item_copy_select_site_list,parent)).also {
                    it.init()
                }
            },object :ItemTypeSet<List<ManagerLocation>>{
                override val viewType: Int
                    get() = ViewType.nothing

                override fun itemBack(parent: ViewGroup): ItemLayout<List<ManagerLocation>>
                =NotSelectListItem(getItemView(R.layout.item_not_copy_select_site_list,parent)).also {
                    it.init()
                }
            })
    }


    private inner class SelectCopyListItem(itemView: View) : ItemLayout<List<ManagerLocation>>(itemView) {

        private var recycleView=itemView.findViewById<BaseRecycleView>(R.id.recycleView)

        override fun init() {
            recycleView.useVerticalView()

        }

        override fun refresh(data: List<ManagerLocation>?) {
            super.refresh(data)
            data.notNull {
                Log.w("SelectCopyListItem list size->${it.size}")
                recycleView.adapter=SelectCopyAadaptor(it)
            }
        }

        private inner class SelectCopyAadaptor(private var list: List<ManagerLocation>) : BaseAdapter<SelectCopyItem>(context) {

            private var selectLocList=ArrayList<ManagerLocation>()

            init {
                setItemListClickListener {
                    var loc=list[it]
                    if (selectLocList.any {l->l.Info?.SerialNumber==loc.Info?.SerialNumber}){
                        selectLocList.remove(selectLocList.first { l->l.Info?.SerialNumber==loc.Info?.SerialNumber })
                    }else{
                        selectLocList.add(loc)
                    }

                    onSelectLoc(selectLocList)
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectCopyItem
            = SelectCopyItem(getItemView(R.layout.item_select_copy_site, parent)).also {
                it.init()
            }

            override fun onBindViewHolder(item: SelectCopyItem, position: Int) {
                super.onBindViewHolder(item, position)
                item.refresh(list[position])
            }

            override fun getItemCount(): Int = list.size
        }

        private inner class SelectCopyItem(itemView: View) : ItemLayout<ManagerLocation>(itemView) {

            private var imgView = itemView.findViewById<AutoLoadImgView>(R.id.imgView)
            private var serialText = itemView.findViewById<TextView>(R.id.serialText)
            private var siteName=itemView.findViewById<TextView>(R.id.siteName)
            private var addressText=itemView.findViewById<TextView>(R.id.addressText)
            private var checkImg=itemView.findViewById<ImageView>(R.id.checkImg)
            private var parentView=itemView.findViewById<View>(R.id.parentView)

            private var isSelect=false

            override fun init() {
                parentView.setOnClickListener {
                    isSelect=!isSelect
                    if (isSelect){
                        checkImg.setImageResource(R.drawable.icon_checkbox_true_green)
                    }else{
                        checkImg.setImageResource(R.drawable.icon_checkbox_false_green)
                    }
                    itemClick()
                }
            }

            override fun refresh(data: ManagerLocation?) {
                super.refresh(data)
                data.notNull { loc ->
                    imgView.loadUrl(loc.imgUrl(),true)
                    serialText.text = loc.Info?.SerialNumber
                    siteName.text=loc.Info?.Content?:""
                    addressText.text=loc?.Address?.shortName?:""
                }
            }
        }
    }

    private class NotSelectListItem(itemView: View) : ItemLayout<List<ManagerLocation>>(itemView) {

        private var recycleView=itemView.findViewById<BaseRecycleView>(R.id.recycleView)


        override fun init() {
            recycleView.useVerticalView()
        }

        override fun refresh(data: List<ManagerLocation>?) {
            super.refresh(data)
            data.notNull {
                Log.i("NotSelectListItem list size->${it.size}")
                recycleView.adapter=NotSelectAadaptor(it)
            }
        }

        private inner class NotSelectAadaptor(private var list:List<ManagerLocation>): BaseAdapter<NotSelectItem>(context) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotSelectItem
            = NotSelectItem(getItemView(R.layout.item_select_copy_site,parent)).also {
                it.init()
            }

            override fun onBindViewHolder(item: NotSelectItem, position: Int) {
                super.onBindViewHolder(item, position)
                item.refresh(list[position])
            }
            override fun getItemCount(): Int =list.size
        }

        private class NotSelectItem(itemView: View) : ItemLayout<ManagerLocation>(itemView) {

            private var imgView=itemView.findViewById<AutoLoadImgView>(R.id.imgView)
            private var serialText=itemView.findViewById<TextView>(R.id.serialText)
            private var siteName=itemView.findViewById<TextView>(R.id.siteName)
            private var addressText=itemView.findViewById<TextView>(R.id.addressText)
            private var checkImg=itemView.findViewById<ImageView>(R.id.checkImg)

            override fun init() {
                checkImg.visibility=View.GONE
            }

            override fun refresh(data: ManagerLocation?) {
                super.refresh(data)
                data.notNull { loc->
                    imgView.loadUrl(loc.imgUrl(),true)
                    serialText.text=loc.Info?.SerialNumber
                    siteName.text=loc.Info?.Content?:""
                    addressText.text=loc?.Address?.shortName?:""
                }
            }
        }
    }
    
    override var theme: PPYPTheme=PPYPTheme.Manager
    override fun setData(data: List<OpenSet>?) {
        var now=DateTime.now()
        //先去除已經過掉的時間
        data.notNull { copyFrom=it.filter {set->set.startDateTime().date>=now.date  } }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragCopyLocOpenBinding.inflate(inflater,container,false)
        return binding
    }
}