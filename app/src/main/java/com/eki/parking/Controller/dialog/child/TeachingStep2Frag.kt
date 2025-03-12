package com.eki.parking.Controller.dialog.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.frag.BaseFragment
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialEvent
import com.eki.parking.Controller.impl.ISerialFinish
import com.eki.parking.R
import com.eki.parking.View.pager.ViewPagerFragAdapter
import com.eki.parking.databinding.FragTeachingStep2Binding
import com.eki.parking.databinding.ItemTeachStepDetailBinding
import com.hill.devlibs.extension.notNull
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.impl.ISetData

/**
 * Created by Hill on 2020/05/05
 */
class TeachingStep2Frag : DialogChildFrag<TeachingStep2Frag>(),
    ISerialDialog, ISerialDialog.TitleSet, ISerialFinish, IFragViewBinding {

    private lateinit var binding: FragTeachingStep2Binding
    private lateinit var serialEvent: ISerialEvent

    override fun initFragView() {
        binding.viewPager.adapter = ViewPagerFragAdapter(childFragmentManager, arrayListOf(
            StepFrag().also {
                it.setData(
                    StepData(
                        1, getString(R.string.teach_title_step1), R.drawable.example01)) },
            StepFrag().also {
                it.setData(
                    StepData(
                        2, getString(R.string.teach_title_step2), R.drawable.example02)) },
            StepFrag().also {
                it.setData(
                    StepData(
                        3, getString(R.string.teach_title_step3), R.drawable.example03)) }
        ))

        binding.pageIndicatorView.setViewPager(binding.viewPager)

        binding.determinBtn.setOnClickListener {
            dissmissDialog()
            onSerialFinish?.invoke()
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragTeachingStep2Binding.inflate(inflater, container, false)
        return binding
    }

    override val frag: DialogChildFrag<*>
        get() = this
    override val title: String
        get() = ""

    override fun setEvent(event: ISerialEvent) {
        serialEvent = event
    }

    override fun next(): ISerialDialog? = null

    override var onSerialFinish: (() -> Unit)? = {}
    override val titleVisible: Boolean
        get() = false

    class StepFrag : BaseFragment<StepFrag>(), ISetData<StepData>, IFragViewBinding {
        private lateinit var binding: ItemTeachStepDetailBinding
        private lateinit var stepDate: StepData

        override fun initFragView() {
            binding.step.text = stepDate.step.toString()
            binding.title.text = stepDate.title
            binding.imgView.setImageResource(stepDate.img)
        }

        override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
            binding = ItemTeachStepDetailBinding.inflate(inflater, container, false)
            return binding
        }

        override fun setData(data: StepData?) {
            data.notNull { stepDate = it }
        }
    }

    data class StepData(val step: Int, val title: String, @DrawableRes val img: Int)
}