package com.eki.parking.Controller.impl

import android.graphics.Color
import android.view.View
import androidx.annotation.*
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.R
import com.eki.parking.extension.dimen

/**
 * Created by Hill on 09,10,2019
 */
interface IDialogFeatureSet<Frag: DialogChildFrag<*>> {
    interface IBtnClickEvent{
        var determinClick:()->Unit
        var cancelClick:()->Unit
    }
    abstract class ITitleBarSet{
        @get:DrawableRes
        abstract val backgroundRes:Int
        @get:ColorInt
        abstract val titleTextColor:Int
        @get:Dimension
        abstract val titleTextSize:Float

        @DrawableRes
        open val closeBtnRes=0

        open val visible=true
    }
    abstract class IContentSet{
        @DrawableRes
        open val backgroundRes:Int= R.drawable.shape_eki_dialog_bottom
    }

    abstract class IBtnFrameSet{
        open val useDefault= true
        open val btnContent:IClickBtnFrame?=null
        open var determinClick={}
        open var cancelClick={}
    }
    abstract class IClickBtnFrame{
        @get:LayoutRes
        abstract val viewRes:Int
        abstract fun setUpView(v:View)
        var dismissClick:(()->Unit)?=null
    }



    val frag:Frag
    val title:String
    //null 可以不設定
    val titleSet:ITitleBarSet?
    val contentSet:IContentSet?
    val btnFrameSet:IBtnFrameSet
//    val checkBtnText:String
    fun onDismissCheck():Boolean
//    var determinClick:()->Unit
//    var cancelClick:()->Unit

    class NoDialogTitle:ITitleBarSet(){
        override val visible: Boolean
            get() = false
        override val backgroundRes: Int
            get() = 0
        override val titleTextColor: Int
            get() = 0
        override val titleTextSize: Float
            get() = dimen(R.dimen.text_size_6)
    }
    class OrangeDialogTitle :ITitleBarSet(){
        override val backgroundRes: Int
            get() = R.drawable.shape_eki_dialog_title_orange
        override val titleTextColor: Int
            get() = Color.WHITE
        override val titleTextSize: Float
            get() = dimen(R.dimen.text_size_6)
        override val closeBtnRes: Int
            get() = R.drawable.icon_close_white
    }
    class GreenDialogTitle :ITitleBarSet(){
        override val backgroundRes: Int
            get() = R.drawable.shape_eki_dialog_title_green
        override val titleTextColor: Int
            get() = Color.WHITE
        override val titleTextSize: Float
            get() = dimen(R.dimen.text_size_6)
        override val closeBtnRes: Int
            get() = R.drawable.icon_close_white
    }

    class DefaultContentSet: IContentSet() {
    }
    class NoBottomBtn:IBtnFrameSet(){
        override val useDefault: Boolean
            get() = false
    }
}