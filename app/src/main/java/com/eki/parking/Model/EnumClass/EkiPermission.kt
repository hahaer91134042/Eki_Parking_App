package com.eki.parking.Model.EnumClass

import android.Manifest.permission.*
import com.eki.parking.Controller.tools.PermissionController
import com.hill.devlibs.annotation.PermissionFieldSet

/**
 * Created by Hill on 27,09,2019
 */
enum class EkiPermission {
    @PermissionFieldSet(CALL_PHONE,PermissionController.CALL_PHONE_CODE)
    Phone,
    @PermissionFieldSet(CAMERA,PermissionController.CAMERA_CODE)
    Camera,
    @PermissionFieldSet(WRITE_EXTERNAL_STORAGE,PermissionController.WRITE_EXTERNAL_STORAGE_CODE)
    WriteExternalStorage,
    @PermissionFieldSet(READ_EXTERNAL_STORAGE,PermissionController.READ_EXTERNAL_STORAGE_CODE)
    ReadExternalStorage,
    @PermissionFieldSet(ACCESS_FINE_LOCATION,PermissionController.FINE_LOCATION_CODE)
    FineLocation,
    @PermissionFieldSet(ACCESS_COARSE_LOCATION,PermissionController.COARSE_LOCATION_CODE)
    CoraseLocation
}