package com.eki.parking.Controller.activity.abs

import android.os.Bundle
import com.eki.parking.View.libs.GlideImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerListener
import java.util.ArrayList

/**
 * Created by Hill on 2019/6/26
 */
abstract class LeftMenuWithBannerActivity:LeftMenuWithTitleBarActivity() {

    private var banner: Banner? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBanner()
    }

    private fun initBanner() {
        banner = findViewById(leftMenuCom.bannerRes)

        val imgList = ArrayList<String>()
        //添加圖片
        imgList.add("https://media.taaze.tw/showBanaerImage.html?pk=1000447874&width=810&height=326")
        imgList.add("https://media.taaze.tw/showBanaerImage.html?pk=1000447532&width=810&height=326")
        imgList.add("https://media.taaze.tw/showBanaerImage.html?pk=1000447932&width=810&height=326")
        imgList.add("https://media.taaze.tw/showBanaerImage.html?pk=1000446922&width=810&height=326")

        banner?.setImageLoader(GlideImageLoader())
        banner?.setIndicatorGravity(BannerConfig.RIGHT)//圓點的位置
        banner?.setImages(imgList)//加載的圖片
                ?.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                ?.setDelayTime(2000)?.start()//圖片循環滑動的時間2秒
        //設置點擊事件
        banner?.setOnBannerListener(OnBannerListener { position ->
            when (position) {
                0 -> showToast("1111")
                1 -> showToast("222")
                2 -> showToast("333")
                3 -> showToast("444")
            }
        })
    }

}