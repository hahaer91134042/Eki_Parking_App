package com.eki.parking.View.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.eki.parking.R
import com.eki.parking.extension.color
import com.eki.parking.extension.dimen
import com.eki.parking.extension.pxToDp
import com.hill.devlibs.extension.mod02d

/**
 * Created by Hill on 2020/05/12
 */
class HourScaleView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    companion object{
        const val startHour=0
        const val endHour=23
    }

    var cellHeight=0f

    private var textPaint= Paint().also {
        it.color= color(R.color.light_gray5)
        it.textSize= dimen(R.dimen.text_size_16)
        it.isAntiAlias=true
    }
    private var linePaint= Paint().also { it.color= color(R.color.light_gray5) }

    var hourScale=ArrayList<HourScale>().apply {
        for (hour in startHour..endHour) {
            add(HourScale(hour))
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var startH=0f
        var startW=0f

        var h=measuredHeight.toFloat()
        var w=measuredWidth.toFloat()
        cellHeight=h/hourScale.size

//            Log.d("dispatchDraw h->$h  w->$w  startH->$startH startW->$startW")

        for (i in hourScale.indices) {
            var scale=hourScale[i]
            scale.h=h
            scale.w=w
            scale.startH=startH+HourScale.lineOffset
            scale.endH=startH+cellHeight
//                Log.w("i->$i scale->$scale")
//                Log.i("startH->$startH")
            when{
                i>0->{
//                        drawRect(float left, float top, float right, float bottom, @NonNull Paint paint)
                    canvas?.drawRect(startW,startH,w,scale.startH,linePaint)

                }
                else->{

                }
            }

            scale.drawHourText(canvas,textPaint)

            scale.drawMinScale(canvas,linePaint)

            startH += cellHeight
        }

    }

    data class HourScale(val hour:Int){

        var minScales= arrayListOf(
                MinScale(0),
                MinScale(15),
                MinScale(30),
                MinScale(45)
        )

        var startH=0f
        var endH=0f
        var w=0f
        var h=0f

        fun drawHourText(canvas: Canvas?, textPaint: Paint) {
            //drawText(@NonNull String text, float x, float y, @NonNull Paint paint)
            canvas?.drawText(hour.mod02d(),textOffset_X,startH+textOffset_Y,textPaint)
        }

        fun drawMinScale(canvas: Canvas?,linePaint: Paint) {
            //先畫刻度

            var textPaint=Paint().also {
                it.isAntiAlias=true
                it.color= color(R.color.light_gray5)
                it.textSize= dimen(R.dimen.text_size_14)
            }
            var startY=startH
            var scaleH=(endH-startH)/minScales.size
            for (i in minScales.indices){
                var scale=minScales[i]
                scale.startX=MinScale.lineOffsetX
                scale.endX=w
                scale.startY=startY
                scale.endY=startY+scaleH+MinScale.lineOffset

                when{
                    i>0->{
                        canvas?.drawRect(MinScale.lineOffsetX,startY,w,startY+MinScale.lineOffset,linePaint)

                    }
                }

                canvas?.drawText(scale.startMin.mod02d(),MinScale.lineOffsetX,scale.startY+MinScale.textOffset_Y,textPaint)

                startY+=scaleH
            }

        }

        companion object{
            const val lineOffset= 3f
            val textOffset_X= 50f
//            const val textOffset_X=50f
            val textOffset_Y = 63f
        }
    }


    data class MinScale(val startMin:Int, val span:Int=15){
        var endMin=startMin+span
        var startX=0f
        var endX=0f
        var startY=0f
        var endY=0f

        companion object{
            const val lineOffset= 3f
            val lineOffsetX= 200f
//            val lineOffsetX=200f
            val textOffset_Y= 63f
        }
    }
}