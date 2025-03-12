package com.eki.parking.View.widget

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import com.eki.parking.R
import com.eki.parking.View.abs.ConstrainCustomView
import com.eki.parking.extension.color


/**
 * Created by Hill on 2021/12/29
 */
class AddOpenTimeView(context: Context?, attrs: AttributeSet?) :
    ConstrainCustomView(context, attrs) {

    companion object {
        const val animDuration = 300L
    }

    interface OnBtnEvent {
        fun onAddSingle()//單筆新增
        fun onAddRepeat()//下周重複
        fun onAddCopy()//複製貼上
    }

    var open = false

    private lateinit var singleModeAnim: IViewAnim
    private lateinit var repeatModeAnim: IViewAnim
    private lateinit var copyModeAnim: IViewAnim
    var btnEvent: OnBtnEvent? = null

    private lateinit var addBtn: ImageButton
    private lateinit var singleBtnFrame: LinearLayout
    private lateinit var repeatBtnFrame: LinearLayout
    private lateinit var copyBtnFrame: LinearLayout
    private lateinit var addSingle: ImageView
    private lateinit var addRepeat: ImageView
    private lateinit var addCopy: ImageView

    override fun initInFlaterView() {
        addBtn = findViewById(R.id.addBtn)
        singleBtnFrame = findViewById(R.id.singleBtnFrame)
        repeatBtnFrame = findViewById(R.id.repeatBtnFrame)
        copyBtnFrame = findViewById(R.id.copyBtnFrame)
        addSingle = findViewById(R.id.addSingle)
        addRepeat = findViewById(R.id.addRepeat)
        addCopy = findViewById(R.id.addCopy)

        addBtn.setOnClickListener {
            setOptionOpen(!open)
        }

        singleModeAnim = LeftTopAnim(singleBtnFrame)
        repeatModeAnim = TopAnim(repeatBtnFrame)
        copyModeAnim = RightTopAnim(copyBtnFrame)

        addSingle.setOnClickListener {
            btnEvent?.onAddSingle()
        }


        addRepeat.setOnClickListener {
            btnEvent?.onAddRepeat()
        }

        addCopy.setOnClickListener {
            btnEvent?.onAddCopy()
        }
    }

    fun setOptionOpen(b: Boolean) {
        open = b
        if (open) {
            openOption()
        } else {
            closeOption()
        }
    }

    private fun openOption() {
        changeBgColor()
        //避免點擊事件穿透
        isClickable = true
        addBtn.animate()
            .rotation(-45f)
            .setDuration(animDuration)
            .setInterpolator(LinearInterpolator())
            .start()

        singleModeAnim.start()
        repeatModeAnim.start()
        copyModeAnim.start()
    }

    private fun closeOption() {
        changeBgColor()
        isClickable = false
        addBtn.animate()
            .rotation(0f)
            .setDuration(animDuration)
            .setInterpolator(LinearInterpolator())
            .start()

        singleModeAnim.back()
        repeatModeAnim.back()
        copyModeAnim.back()
    }

    private fun changeBgColor() {
        if (open) {
            setBackgroundColor(color(R.color.light_gray2))
        } else {
            setBackgroundColor(color(R.color.color_transparent))
        }
    }

    override fun setInflatView(): Int = R.layout.item_add_open_time_option_view

    override fun initNewLayoutParams(): ViewGroup.LayoutParams? = null

    private inner class RightTopAnim(private var view: View) : IViewAnim {
        init {
            view.visibility = INVISIBLE
        }

        override fun animInterpolator(): Interpolator = AccelerateInterpolator()

        override val duration: Long
            get() = animDuration

        override fun start() {
            var animX = 180f
            var animY = -120f

            view.animate()
                .translationX(animX)
                .translationY(animY)
                .setInterpolator(animInterpolator())
                .setDuration(duration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        view.visibility = VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {

                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }
                }).start()
        }

        override fun back() {
            view.animate()
                .translationX(0f)
                .translationY(0f)
                .setInterpolator(animInterpolator())
                .setDuration(duration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = INVISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }
                }).start()
        }
    }

    private inner class TopAnim(private var view: View) : IViewAnim {
        init {
            view.visibility = INVISIBLE
        }

        override fun animInterpolator(): Interpolator = AccelerateInterpolator()

        override val duration: Long
            get() = animDuration

        override fun start() {
            view.animate()
                .translationX(0f)
                .translationY(-200f)
                .setInterpolator(animInterpolator())
                .setDuration(duration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        view.visibility = VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {

                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }
                }).start()
        }

        override fun back() {
            view.animate()
                .translationX(0f)
                .translationY(0f)
                .setInterpolator(animInterpolator())
                .setDuration(duration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = INVISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }
                }).start()
        }
    }

    private inner class LeftTopAnim(private var view: View) : IViewAnim {

        init {
            view.visibility = INVISIBLE
        }

        override fun animInterpolator(): Interpolator = AccelerateInterpolator()

        override val duration: Long
            get() = animDuration

        override fun start() {
            var animX = -180f
            var animY = -120f

            view.animate()
                .translationX(animX)
                .translationY(animY)
                .setInterpolator(animInterpolator())
                .setDuration(duration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {
                        view.visibility = VISIBLE
                    }

                    override fun onAnimationEnd(animation: Animator?) {

                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }
                }).start()
        }

        override fun back() {
            view.animate()
                .translationX(0f)
                .translationY(0f)
                .setInterpolator(animInterpolator())
                .setDuration(duration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = INVISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator?) {

                    }

                    override fun onAnimationRepeat(animation: Animator?) {

                    }
                }).start()

        }
    }

    private interface IViewAnim {
        fun animInterpolator(): Interpolator
        val duration: Long
        fun start()
        fun back()
    }
}