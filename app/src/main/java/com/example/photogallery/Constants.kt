package com.example.photogallery
import android.os.Build
import android.Manifest
object Constants {
    const val TAG = "Gallery Photo"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    val REQUIRED_PERMISSIONS =
        mutableListOf (
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
}