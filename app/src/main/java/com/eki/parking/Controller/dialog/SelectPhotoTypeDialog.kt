package com.eki.parking.Controller.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.eki.parking.Controller.dialog.abs.DialogChildFrag
import com.eki.parking.Controller.impl.IAppTheme
import com.eki.parking.Controller.impl.IDialogFeatureSet
import com.eki.parking.Model.EnumClass.PPYPTheme
import com.eki.parking.databinding.DialogSelectPhotoTypeBinding
import com.hill.devlibs.impl.IFragViewBinding

/**
 * Created by Hill on 2020/10/27
 */
class SelectPhotoTypeDialog(theme: PPYPTheme = PPYPTheme.Manager) : FragContainerDialog() {
    private var childFrag = PhotoTypeChildFrag().also { it.theme = theme }

    init {
        setContent(childFrag)
    }

    var onSelectCamera: (() -> Unit)? = null
        set(value) {
            childFrag.onSelectCamera = value
        }
    var onSelectAlbum: (() -> Unit)? = null
        set(value) {
            childFrag.onSelectAlbum = value
        }

    class PhotoTypeChildFrag : DialogChildFrag<PhotoTypeChildFrag>(),
        IDialogFeatureSet<PhotoTypeChildFrag>, IAppTheme, IFragViewBinding {

        override var theme: PPYPTheme = PPYPTheme.Manager
        var onSelectCamera: (() -> Unit)? = null
        var onSelectAlbum: (() -> Unit)? = null
        private lateinit var binding:DialogSelectPhotoTypeBinding

        override val frag: PhotoTypeChildFrag
            get() = this
        override val title: String
            get() = "請選擇開啟方式"
        override val titleSet: IDialogFeatureSet.ITitleBarSet?
            get() = theme.dialogTitle()
        override val contentSet: IDialogFeatureSet.IContentSet?
            get() = IDialogFeatureSet.DefaultContentSet()
        override val btnFrameSet: IDialogFeatureSet.IBtnFrameSet
            get() = IDialogFeatureSet.NoBottomBtn()

        override fun onDismissCheck(): Boolean {
            return true
        }

        override fun initFragView() {
            binding.openCamera.setOnClickListener {
                onSelectCamera?.invoke()
                dissmissDialog()
            }
            binding.openAlbum.setOnClickListener {
                onSelectAlbum?.invoke()
                dissmissDialog()
            }
        }

        override fun fragViewBinding(inflater: LayoutInflater, container: ViewGroup): ViewBinding {
            binding = DialogSelectPhotoTypeBinding.inflate(inflater,container,false)
            return binding
        }
    }
}