package com.eki.parking.Controller.activity.frag.Reserva

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import com.eki.parking.AppConfig
import com.eki.parking.Controller.frag.AutoLoadImgFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.Model.DTO.LocationImg
import com.eki.parking.Model.sql.EkiLocation
import com.eki.parking.Model.sql.EkiMember
import com.eki.parking.R
import com.eki.parking.View.pager.ViewPagerFragAdapter
import com.eki.parking.View.widget.EkiAdView
import com.eki.parking.View.widget.SelectFrameImgView
import com.eki.parking.databinding.FragLocationDetailPageBinding
import com.eki.parking.extension.loadImgInto
import com.eki.parking.extension.sqlHasData
import com.eki.parking.extension.string
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/09/30
 */
class LocationDetailFrag : SearchFrag(),
    ISetData<EkiLocation>, IFragViewBinding {

    private lateinit var binding: FragLocationDetailPageBinding
    private lateinit var location: EkiLocation
    private lateinit var frameCtrl: ImgFrameCtrl
    var onGoReserva = {}

    private lateinit var adView: EkiAdView

    override fun initFragView() {
        frameCtrl = ImgFrameCtrl()

        binding.ratingBar.setRatingStar(location.RatingCount)

        binding.priceTextView.text =
            getString(R.string.billing_method_half_hour,location.Config?.Price?.toInt() ?: 0)

        if (location.Img.size > 0) {
            var list = location.Img.sortedBy { it.Sort }

            for (i in list.indices) {
                when (i) {
                    0 -> frameCtrl.addImg(list[i], binding.subImg1)
                    1 -> frameCtrl.addImg(list[i], binding.subImg2)
                    2 -> frameCtrl.addImg(list[i], binding.subImg3)
                }
            }
            frameCtrl.start()
        }

        binding.locIntroduceView.setLoc(location)
        binding.locIntroduceView.storeInfoBtn.setOnClickListener {
            binding.adFrame.visibility = View.VISIBLE
        }

        binding.determinBtn.setOnClickListener {
            setCustomDialog()
        }

        adView = EkiAdView(context)
        adView.adLoadEvent = binding.locIntroduceView
        adView.bindView(binding.adFrame)
        adView.loadUrl(AppConfig.Url.AD_URL + location.Info?.SerialNumber)
    }

    override fun onResumeFragView() {
        toolBarTitle = string(R.string.Parking_information)
    }

    override fun setData(data: EkiLocation?) {
        data.notNull { location = it }
    }


    private inner class ImgFrameCtrl() {
        var viewMap = ArrayList<Pair<LocationImg, SelectFrameImgView>>()


        fun addImg(img: LocationImg, view: SelectFrameImgView) {
            viewMap.add(Pair(img, view))
        }

        fun start() {
            binding.viewPager.adapter = ViewPagerFragAdapter<AutoLoadImgFrag>(childFragmentManager,
                when {
                    viewMap.size > 0 -> viewMap.map { pair ->
                        pair.first.loadImgInto(pair.second.imgView, true)
                        pair.second.parentView.setOnClickListener {
                            pair.second.setSelect(true)
                            viewMap.filterNot { it.first.Sort == pair.first.Sort }.forEach {
                                it.second.setSelect(false)
                            }
                            binding.viewPager.currentItem = viewMap.indexOf(pair)
                        }

                        AutoLoadImgFrag().apply {
                            setImgUrl(pair.first.Url, true)
                        }
                    }
                    else -> arrayListOf(AutoLoadImgFrag().also {
                        it.setImgUrl("", true)
                    })
                }
            )


            viewMap[0]?.second?.setSelect(true)

            binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    var select = viewMap[position]
                    select.second.setSelect(true)
                    viewMap.filterNot { it == select }
                        .forEach {
                            it.second.setSelect(false)
                        }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

        }

    }

    private fun setCustomDialog() {
        val builder = AlertDialog.Builder(requireContext())
            .create()
        val dialogView = layoutInflater.inflate(R.layout.dialog_base_msg, null)
        val reserveButton = dialogView.findViewById<Button>(R.id.confirm_button)
        val text = dialogView.findViewById<TextView>(R.id.msg_text)
        builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        text.text = getString(R.string.Appointment_advice)
        text.textAlignment = View.TEXT_ALIGNMENT_CENTER
        text.gravity = Gravity.CENTER

        reserveButton.text = getString(R.string.reserve)
        reserveButton.setTextColor(ResourcesCompat.getColor(resources,R.color.Eki_orange_4,null))
        reserveButton.setOnClickListener{
            if (sqlHasData<EkiMember>()) {
                onGoReserva()
            } else {
                showToast(string(R.string.Please_log_in_as_a_member_to_make_an_appointment))
            }
            builder.dismiss()
        }
        builder.setView(dialogView)

        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragLocationDetailPageBinding.inflate(inflater, container, false)
        return binding
    }
}