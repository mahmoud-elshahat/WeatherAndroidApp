package com.mahmoudelshahat.weatherandroidapp.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    fun requestGrantedPermissions(
        activity: AppCompatActivity?,
        permissions: Array<String?>?,
        requestCode: Int
    ): Boolean {
        var requestPermission = true
        if (!isPermissionsGranted(activity, permissions)) {
            ActivityCompat.requestPermissions(activity!!, permissions!!, requestCode)
        } else {
            requestPermission = false
        }
        return requestPermission
    }
    fun isPermissionsGranted(context: Context?, grantPermissions: Array<String?>?): Boolean {
        var accessGranted = true
        if (grantPermissions == null || grantPermissions.isEmpty()) {
            accessGranted = false
        } else {
            for (permission in grantPermissions) {
                if (ContextCompat.checkSelfPermission(context!!, permission!!)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    accessGranted = false
                    break
                }
            }
        }
        return accessGranted
    }
}