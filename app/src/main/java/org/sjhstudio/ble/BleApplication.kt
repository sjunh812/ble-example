package org.sjhstudio.ble

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.sjhstudio.ble.util.Val

class BleApplication: Application() {

    companion object {

        lateinit var instance: BleApplication

        fun requestPermission(activity: Activity) {
            val needPermissionList = checkPermission()

            if(needPermissionList.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    activity,
                    needPermissionList.toTypedArray(),
                    Val.REQ_PERMISSION
                )
            }
        }

        fun checkPermission(): List<String> {
            val needPermissionList = mutableListOf<String>()

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if(ContextCompat.checkSelfPermission(instance, Manifest.permission.BLUETOOTH_SCAN)
                    !=  PackageManager.PERMISSION_GRANTED) {
                    needPermissionList.add(Manifest.permission.BLUETOOTH_SCAN)
                }
                if(ContextCompat.checkSelfPermission(instance, Manifest.permission.BLUETOOTH_CONNECT)
                    !=  PackageManager.PERMISSION_GRANTED) {
                    needPermissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
                }
                if(ContextCompat.checkSelfPermission(instance, Manifest.permission.BLUETOOTH_ADVERTISE)
                    !=  PackageManager.PERMISSION_GRANTED) {
                    needPermissionList.add(Manifest.permission.BLUETOOTH_ADVERTISE)
                }
            }

            if(ContextCompat.checkSelfPermission(instance, Manifest.permission.ACCESS_FINE_LOCATION)
                !=  PackageManager.PERMISSION_GRANTED) {
                needPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            return needPermissionList
        }
    }

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

}