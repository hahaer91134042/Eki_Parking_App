package com.eki.parking.Controller.activity.frag.DiscountSelect

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.activity.frag.DiscountSelect.child.CodeInputFrag
import com.eki.parking.Controller.activity.frag.DiscountSelect.child.QrScanFrag
import com.eki.parking.Controller.frag.SearchFrag
import com.eki.parking.R
import com.eki.parking.databinding.FragDiscountInputBinding
import com.hill.devlibs.frag.FragController.FragSwitcher
import com.hill.devlibs.impl.IFragViewBinding
import com.hill.devlibs.tools.Log

/**
 * Created by Hill on 2020/08/25
 */
class DiscountInputFrag : SearchFrag(), IFragViewBinding {

    private lateinit var binding: FragDiscountInputBinding

    override fun initFragView() {
        binding.inputMethodSelector.setOnCheckedChangeListener { group, checkedId ->
            Log.d("select method->$checkedId")
            switchInputFrag(checkedId)
        }

        switchInputFrag(binding.inputMethodSelector.checkedRadioButtonId)
    }

    private fun switchInputFrag(@IdRes id: Int) {
        when (id) {
            R.id.serialInput -> {
                replaceFragWithCacheChildFrag(
                    ChildCacheLevel(1)
                        .setFrag(CodeInputFrag()),
                    FragSwitcher.SWITCH_FADE
                )
            }
            else -> {
                replaceFragWithCacheChildFrag(
                    ChildCacheLevel(1)
                        .setFrag(QrScanFrag()),
                    FragSwitcher.SWITCH_FADE
                )
            }
        }
    }

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragDiscountInputBinding.inflate(inflater, container, false)
        return binding
    }
}