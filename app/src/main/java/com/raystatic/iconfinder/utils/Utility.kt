package com.raystatic.iconfinder.utils

import android.Manifest
import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

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


}