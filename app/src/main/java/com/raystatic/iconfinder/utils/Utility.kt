package com.raystatic.iconfinder.utils

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

object Utility {

    fun hasWritePermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    fun hasReadPermission(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

    fun getOutputDirectory(context: Context): File? {
        val contextWrapper = ContextWrapper(context)
       // return contextWrapper.getDir(Constants.ICONS_DIRECTORY,Context.MODE_PRIVATE)
        return contextWrapper.filesDir
    }

    fun fileSizeInMb(file: File): String {
        val size = file.length()

        val sizeInMb = size / (1024.0 * 1024)
        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.CEILING
        val formattedSize = df.format(sizeInMb)

        return "$formattedSize MB"
    }


}