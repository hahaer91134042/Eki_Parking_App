package com.eki.parking.Controller.activity.frag.Main.child

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.*
import com.eki.parking.Controller.activity.intent.SiteListIntent
import com.eki.parking.Controller.frag.ChildFrag
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.View.ViewType
import com.eki.parking.View.recycleview.adapter.ViewTypeAdaptor
import com.eki.parking.View.recycleview.item.ItemLayout
import com.eki.parking.databinding.ItemRecycleviewBinding
import com.eki.parking.extension.sqlData
import com.hill.devlibs.extension.isEmpty
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.IRecycleViewModelSet
import com.squareup.picasso.Picasso

/**
 * Created by Hill on 2020/05/26
 */
class SettingPanelFrag : ChildFrag(), IFragViewBinding {

    private lateinit var binding: ItemRecycleviewBinding
    private var member: EkiMember? = null
    private var list = ArrayList<ItemSet>()

    override fun initFragView() {
        member = sqlData()
        binding.recycleView.useVerticalView()
        binding.recycleView.adapter = SettingItemAdaptor()

        list.add(ItemSet(ViewType.title, member))
        list.add(ItemSet(ViewType.item, member))
        if (member?.beManager == true)
            list.add(ItemSet(ViewType.item2, member))
        list.add(ItemSet(ViewType.item3, member))

    }

    private val onEditUserInfo = {
        startActivitySwitchAnim(EditUserActivity::class.java)
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = ItemRecycleviewBinding.inflate(inflater, container, false)
        return binding
    }

    private inner class SettingItemAdaptor : ViewTypeAdaptor<EkiMember>(context) {
        override val modelList: ModelList<EkiMember>
            get() = ModelList(list)


        override val viewSets: SetList<EkiMember>
            get() = SetList(object : ItemTypeSet<EkiMember> {
                override val viewType: Int
                    get() = ViewType.title

                override fun itemBack(parent: ViewGroup): ItemLayout<EkiMember> =
                    UserHeaderItem(getItemView(R.layout.item_setting_member_header, parent))
                        .also { it.init() }
            }, object : ItemTypeSet<EkiMember> {
                override val viewType: Int
                    get() = ViewType.item

                override fun itemBack(parent: ViewGroup): ItemLayout<EkiMember> =
                    CarUserItem(getItemView(R.layout.item_car_user_setting, parent))
                        .also { it.init() }
            }, object : ItemTypeSet<EkiMember> {
                override val viewType: Int
                    get() = ViewType.item2

                override fun itemBack(parent: ViewGroup): ItemLayout<EkiMember> =
                    ManagerSetItem(getItemView(R.layout.item_manager_user_setting, parent))
                        .also { it.init() }
            }, object : ItemTypeSet<EkiMember> {
                override val viewType: Int
                    get() = ViewType.item3

                override fun itemBack(parent: ViewGroup): ItemLayout<EkiMember> =
                    AboutMeItem(getItemView(R.layout.item_customer_service, parent))
                        .also { it.init() }
            })

    }

    private data class ItemSet(override val viewType: Int, override val data: EkiMember?) :
        IRecycleViewModelSet<EkiMember>

    private inner class UserHeaderItem(itemView: View) : ItemLayout<EkiMember>(itemView) {

        private var icon = itemView.findViewById<ImageView>(R.id.iconImg)
        private var name = itemView.findViewById<TextView>(R.id.userNameText)
        private var editBtn = itemView.findViewById<Button>(R.id.editBtn)

        override fun init() {
            editBtn.setOnClickListener {
                onEditUserInfo()
            }
        }

        override fun refresh(data: EkiMember?) {
            super.refresh(data)

            data.notNull { member ->
                if (!member.info?.IconImg.isNullOrEmpty())
                    Picasso.with(context)
                        .load(member.info?.IconImg)
                        .placeholder(R.drawable.none_img)
                        .into(icon)

                name.text = member.info?.NickName
            }
        }
    }

    private inner class CarUserItem(itemView: View) : ItemLayout<EkiMember>(itemView) {
        var myCarRow = itemView.findViewById<View>(R.id.myCarRow)
        var discountRow = itemView.findViewById<View>(R.id.discountRow)
        override fun init() {
            myCarRow.setOnClickListener {
                startActivitySwitchAnim(CarSettingActivity::class.java, false)
            }
            discountRow.setOnClickListener {
                startActivitySwitchAnim(MemberDiscountActivity::class.java, false)
            }
        }

        override fun refresh(data: EkiMember?) {
            super.refresh(data)


        }
    }

    private inner class ManagerSetItem(itemView: View) : ItemLayout<EkiMember>(itemView) {

        private var siteSetRow = itemView.findViewById<View>(R.id.siteSetRow)
        private var bankRow = itemView.findViewById<View>(R.id.bankRow)
        private var rowFrame = itemView.findViewById<LinearLayout>(R.id.rowFrame)

        private var referrerRow = itemView.findViewById<View>(R.id.referrerRow)
        private var referrerText = itemView.findViewById<TextView>(R.id.referrerText)
        private var referrerArrow = itemView.findViewById<ImageView>(R.id.referrerArrow)

        override fun init() {
            registerReceiver(AppFlag.OnReferrerAdd)

            siteSetRow.setOnClickListener {
                startActivitySwitchAnim(SiteListIntent(context), false)
            }
            bankRow.setOnClickListener {
                startActivitySwitchAnim(EditBankActivity::class.java, false)
            }

            referrerRow.setOnClickListener {
                itemData.notNull { member ->
                    if (member.referrer.isEmpty())
                        startActivitySwitchAnim(AddReferActivity::class.java, false)
                }
            }
        }

        override fun refresh(data: EkiMember?) {
            super.refresh(data)
            data.notNull { member ->
                when (member.referrer.isEmpty()) {
                    false -> {
                        referrerArrow.visibility = View.INVISIBLE
                        referrerText.text = member.referrer
                    }
                    else -> {
                        referrerArrow.visibility = View.VISIBLE
                        referrerText.text = ""
                    }
                }
            }
        }

        override fun onCatchReceiver(action: String?, intent: Intent?) {
            rowFrame.removeView(referrerRow)
        }
    }

    private inner class AboutMeItem(itemView: View) : ItemLayout<EkiMember>(itemView) {
        private var rowFrame = itemView.findViewById<View>(R.id.rowFrame)
        override fun init() {
            rowFrame.setOnClickListener {
                startActivitySwitchAnim(AboutMeActivity::class.java, false)
            }
        }

        override fun refresh(data: EkiMember?) {
            super.refresh(data)
        }
    }
}