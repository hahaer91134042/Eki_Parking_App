package com.eki.parking.View.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.eki.parking.R
import com.eki.parking.extension.color
import com.eki.parking.extension.string
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hill.devlibs.extension.notNull


/**
 * Created by Hill on 2021/02/02
 */
class SaveTextFloatBtn(context: Context, attrs: AttributeSet?) : FloatingActionButton(context, attrs) {

    init {

    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        var text= string(R.string.Save)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = width*2/5f
        paint.color = Color.WHITE
//        paint.textAlign = Paint.Align.CENTER

//        val baseline: Float = -paint.ascent() // ascent() is negative
//        val textW = (paint.measureText(text)).toInt() // round
//        val textH = (baseline + paint.descent()).toInt()
//        Log.w("TextFloatBtn w->$width tW->$textW H->$height tH->$textH")
//        var x=(width/2f)-(textW/2f)
//        var y=(height/2f)-(textH/2f)
//        val xPos = canvas!!.width / 2f
//        val yPos = (canvas!!.height / 2f - (paint.descent() + paint.ascent()) / 2f)
//        Log.i("TextFloatBtn x->$x y->$y")

//        canvas?.drawText(text, xPos, yPos, paint)
        canvas.notNull { c->
            drawCenter(c, paint, text)
        }

    }

    /*
    * https://stackoverflow.com/questions/11120392/android-center-text-on-canvas
    * */
    private fun drawCenter(canvas: Canvas, paint: Paint, text: String) {
        val r = Rect()
        canvas.getClipBounds(r)
        val cHeight: Int = r.height()
        val cWidth: Int = r.width()
        paint.textAlign = Paint.Align.LEFT
        paint.getTextBounds(text, 0, text.length, r)
        val x: Float = cWidth / 2f - r.width() / 2f - r.left
        val y: Float = cHeight / 2f + r.height() / 2f - r.bottom
        canvas.drawText(text, x, y, paint)
    }

    //method to convert your text to image
    private fun textAsBitmap(text: String, textSize: Float, textColor: Int): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize
        paint.color = textColor
        paint.textAlign = Paint.Align.LEFT
        val baseline: Float = -paint.ascent() // ascent() is negative
        val width = (paint.measureText(text)).toInt() // round
        val height = (baseline + paint.descent()).toInt()
        val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(image)
        canvas.drawCircle(width / 2f, height / 2f, when {
            width > height -> height.toFloat()
            else -> width.toFloat()
        }, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = color(R.color.Eki_green_2) })
        canvas.drawText(text, 0f, baseline, paint)

        return image
    }
}