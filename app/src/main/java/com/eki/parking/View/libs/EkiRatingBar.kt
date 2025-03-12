package com.eki.parking.View.libs

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.appcompat.widget.AppCompatImageView
import com.eki.parking.R
import com.eki.parking.View.abs.LinearCustomView

/**
 * Created by Hill on 2020/10/06
 */
class EkiRatingBar(context: Context?, attrs: AttributeSet?) : LinearCustomView(context, attrs) {

    private var fullStarRes=0
    private var halfStarRes=0
    private var emptyStarRes=0
    private var maxStarNum=0
    private var startWidth:Float=0f
    private var startHeight:Float=0f
    private var starMarginStart=0f
    private var starMarginEnd=0f

    private var isEmpty=true

    override fun parseTypedArray(typedArray: TypedArray) {
//        Log.i("parseTypedArray typedArr->$typedArray")
        fullStarRes=typedArray?.getResourceId(R.styleable.EkiRatingBar_fullStartIcon,0)?:0
        halfStarRes=typedArray?.getResourceId(R.styleable.EkiRatingBar_halfStarIcon,0)?:0
        emptyStarRes=typedArray?.getResourceId(R.styleable.EkiRatingBar_emptyStarIcon,0)?:0
        maxStarNum=typedArray?.getInt(R.styleable.EkiRatingBar_maxStarNumber,0)?:0
        startHeight=typedArray?.getDimension(R.styleable.EkiRatingBar_startHeight,0f)?:0f
        startWidth=typedArray?.getDimension(R.styleable.EkiRatingBar_startWidth,0f)?:0f
        starMarginStart=typedArray?.getDimension(R.styleable.EkiRatingBar_starMarginStart,0f)?:0f
        starMarginEnd=typedArray?.getDimension(R.styleable.EkiRatingBar_starMarginEnd,0f)?:0f
    }


    fun setRatingStar(@FloatRange(from = 0.0,to = 10.0) num:Double){
        //Log.d("set rating start->$num")
        //Log.w("maxStart->$maxStarNum start h->$startHeight w->$startWidth")
        var rating=num
        if (rating>maxStarNum)
            rating=maxStarNum.toDouble()
//            throw IllegalArgumentException("Rating must less than max star number")

//        Log.w("rating.toInt()->${rating.toInt()} residue->${(rating*10.0).toInt()%10}")

        if (isEmpty){
            for(i in 1..maxStarNum){
                addView(StarImg(context).apply {
                    setImageResource(when{
                        rating>=i->fullStarRes
                        else->when (i) {
                            rating.toInt()+1 -> {
                                var residue=(rating*10.0).toInt()%10
//                                Log.w("residue->$residue")
                                if (residue>=5)
                                    halfStarRes
                                else
                                    emptyStarRes
                            }
                            else -> emptyStarRes
                        }
                    })
                })
            }
            isEmpty=false
        }

    }

    private inner class StarImg(context: Context) : AppCompatImageView(context){
        init {
            layoutParams= LayoutParams(startWidth.toInt(),startHeight.toInt()).apply {
                setMargins(starMarginStart.toInt(),0,starMarginEnd.toInt(),0)
            }

            setBackgroundColor(Color.WHITE)
        }
    }

    override fun setStyleableRes(): IntArray? = R.styleable.EkiRatingBar
    override fun setInflatView(): Int =0
    override fun initNewLayoutParams(): ViewGroup.LayoutParams? =null
}