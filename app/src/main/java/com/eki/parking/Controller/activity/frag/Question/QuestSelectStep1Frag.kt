package com.eki.parking.Controller.activity.frag.Question

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.R
import com.eki.parking.databinding.FragQuestionSelectStep1Binding
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/10/08
 */

class QuestSelectStep1Frag:SearchFrag(),IFragViewBinding {

    private lateinit var binding: FragQuestionSelectStep1Binding
    var onQuestion:(()->Unit)?=null
    var onFeedback:(()->Unit)?=null

    override fun initFragView() {
        binding.questionBtn.setOnClickListener {
            onQuestion?.invoke()
        }

        binding.feedbackBtn.setOnClickListener {
            onFeedback?.invoke()
        }
    }

    override fun onResumeFragView() {
        toolBarTitle=getString(R.string.Questions_and_reactions)
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragQuestionSelectStep1Binding.inflate(inflater,container,false)
        return binding
    }
}