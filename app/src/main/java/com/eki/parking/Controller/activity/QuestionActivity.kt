package com.eki.parking.Controller.activity

import android.view.Window
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import com.eki.parking.AppConfig
import com.eki.parking.AppFlag
import com.eki.parking.Controller.activity.abs.TitleBarActivity
import com.eki.parking.Controller.activity.frag.Question.QuestSelectStep1Frag
import com.eki.parking.Controller.activity.frag.Question.QuestSelectStep2Frag
import com.eki.parking.Controller.tools.EkiMail
import com.eki.parking.R
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IActivityFeatureSet

/**
 * Created by Hill on 2020/10/08
 */

class QuestionActivity:TitleBarActivity(),TitleBarActivity.SetToolbar{

    companion object{
        const val NoneLogin="NoneLogin"
        const val MemberQuestion="Member"
        const val ManagerQuestion="Manager"
    }

    private lateinit var flag:String
    override fun initActivityView() {

        flag=intent.getStringExtra(AppFlag.DATA_FLAG)?:""

        setTheme()

        replaceFragment(FragLevelSet(1).setFrag(QuestSelectStep1Frag().apply {
            onQuestion = goQuestionFrag
            onFeedback = goFeedBack
        }), FragSwitcher.NON)
    }

    //support@ppyp.app
    private val goFeedBack:()->Unit={

        app.mailManager.send(EkiMail.support())

    }

    private val goQuestionFrag={

        replaceFragment(FragLevelSet(2).setFrag(when (flag) {
            ManagerQuestion -> QuestSelectStep2Frag().apply { setData(AppConfig.Url.managerQuestion) }
            else -> QuestSelectStep2Frag().apply { setData(AppConfig.Url.memberQuestion) }
        }), FragSwitcher.RIGHT_IN)

    }

    override fun setUpResumeComponent() {}

    override fun setUpActivityView(): Int =R.layout.activity_title_bar

    override fun setActivityFeature(): IActivityFeatureSet =object :IActivityFeatureSet(){
        override val menuRes: Int
            get() = R.menu.menu_not_thing
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        toolbar.setBackgroundColor(when(flag){
            ManagerQuestion->getColor(R.color.Eki_green_2)
            else->getColor(R.color.Eki_orange_4)
        })
    }

    private fun setTheme() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        when (flag) {
            ManagerQuestion -> {
                setTheme(R.style.AppThemeGreen)
                window.statusBarColor = resources.getColor(R.color.Eki_green_2, theme)
            }
            else -> {
                setTheme(R.style.AppThemeOrange)
                window.statusBarColor = resources.getColor(R.color.Eki_orange_4, theme)
            }
        }
    }

}
