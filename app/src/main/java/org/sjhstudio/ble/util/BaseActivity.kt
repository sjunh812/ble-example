package org.sjhstudio.ble.util

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import org.sjhstudio.ble.R
import kotlin.coroutines.CoroutineContext

open class BaseActivity: AppCompatActivity(), View.OnClickListener,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onClick(v: View?) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.forEachIndexed { index, permission ->
            // 0: 허용, -1: 거부
            when(permission) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    println("xxx ACCESS_FINE_LOCATION : ${grantResults[index]}")
                    if(grantResults[index] != 0) {
                        Utils.showYesOrNoDialog(this,
                            getString(R.string.need_location_permission),
                            getString(R.string.request_location_permission),
                            { _, _ ->
                                locationPermissionResult.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                            },
                            { _, _ ->
                                Snackbar.make(findViewById(android.R.id.content), getString(R.string.request_location_permission), 1500).show()
                            }
                        )
                    }
                }

                Manifest.permission.BLUETOOTH_SCAN -> {
                    // scan, connect, advertise 모두 기기검색 권한으로 묶여있음.
                    println("xxx BLUETOOTH_SCAN : ${grantResults[index]}")
                    if(grantResults[index] != 0) {
                        Utils.showYesOrNoDialog(this,
                            getString(R.string.need_bluetooth_permission),
                            getString(R.string.request_bluetooth_permission),
                            { _, _ ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri = Uri.fromParts("package", packageName, null)

                                intent.data = uri
                                bluetoothPermissionResult.launch(intent)
                            },
                            { _, _ ->
                                Snackbar.make(findViewById(android.R.id.content), getString(R.string.request_bluetooth_permission), 1500).show()
                            }
                        )
                    }
                }
            }

        }
    }

    private val locationPermissionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { ar ->
        if(Utils.checkPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            println("xxx locationPermissionResult OK!!")
        } else {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.request_location_permission), 1500).show()
        }
    }

    private val bluetoothPermissionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { ar ->
        if(Utils.checkPermissionGranted(this, Manifest.permission.BLUETOOTH_SCAN) &&
            Utils.checkPermissionGranted(this, Manifest.permission.BLUETOOTH_CONNECT) &&
            Utils.checkPermissionGranted(this, Manifest.permission.BLUETOOTH_ADVERTISE)
        ) {
            println("xxx bluetoothScanPermissionResult OK!!")
        } else {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.request_bluetooth_permission), 1500).show()
        }
    }

}