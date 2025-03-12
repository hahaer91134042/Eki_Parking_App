package com.eki.parking.Controller.dialog.child

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.ISerialDialog
import com.eki.parking.Controller.impl.ISerialEvent
import com.eki.parking.databinding.FragTeachingStep1Binding
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/05/05
 */
class TeachingStep1Frag:DialogChildFrag<TeachingStep1Frag>(),
                        ISerialDialog,ISerialDialog.TitleSet,IFragViewBinding{

    private lateinit var binding:FragTeachingStep1Binding
    private lateinit var serialEvent:ISerialEvent

    override fun initFragView() {
        binding.determinBtn.setOnClickListener {
            serialEvent.onNext()
        }
    }

    override val frag: DialogChildFrag<*>
        get() = this
    override val title: String
        get() = ""

    override fun setEvent(event: ISerialEvent) {
        serialEvent=event
    }

    override fun next(): ISerialDialog? =TeachingStep2Frag()
    override val titleVisible: Boolean
        get() = false

    override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
        binding = FragTeachingStep1Binding.inflate(inflater,container,false)
        return binding
    }
}