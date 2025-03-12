package com.eki.parking.Controller.impl


import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.eki.parking.App
import com.eki.parking.Controller.manager.SysCameraManager
import com.hill.devlibs.time.DateTime
import java.io.File

/**
 * Created by Hill on 19,11,2019
 */
abstract class ICameraFileSet {
    open val saveDir: File=SysCameraManager.appCameraDir
    abstract val fileName:String

    open val scaleX:Int = 480
    open val scaleY:Int = 480
    open val addDateLabel=false
    open val labelTime:DateTime by lazy { DateTime() }

    val saveFile:File by lazy { File(saveDir.path, "$fileName.jpg") }
    val tempFileUri: Uri by lazy { getUri() }
    val tempFile:File by lazy { File(SysCameraManager.appCameraTempDir.path, "$fileName.jpg") }

    //所有使用系統相機的相片都先暫存到temp 在resize
    private fun getUri():Uri{
        var from=App.getInstance()
        return when{
            Build.VERSION.SDK_INT >= 24->FileProvider.getUriForFile(from, from.packageName + ".fileprovider", tempFile)
            else->Uri.fromFile(tempFile)
        }
    }
}