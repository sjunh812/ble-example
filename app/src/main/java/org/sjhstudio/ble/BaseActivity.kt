package org.sjhstudio.ble

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.sjhstudio.ble.util.Utils

open class BaseActivity: AppCompatActivity(), View.OnClickListener {

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
                    Utils.showYesOrNoDialog(this,
                        getString(R.string.need_location_permission),
                        getString(R.string.request_location_permission),
                        { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)

                            intent.data = uri
                            locationPermissionResult.launch(intent)
//                            locationPermissionResult.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        },
                        { _, _ ->
                            Snackbar.make(findViewById(android.R.id.content), getString(R.string.request_location_permission), 1500).show()
                        }
                    )
                }

                Manifest.permission.BLUETOOTH_CONNECT -> {
                    println("xxx BLUETOOTH_CONNECT : ${grantResults[index]}")
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

                Manifest.permission.BLUETOOTH_SCAN -> {
                    println("xxx BLUETOOTH_SCAN : ${grantResults[index]}")
                    Utils.showYesOrNoDialog(this,
                        getString(R.string.need_bluetooth_scan_permission),
                        getString(R.string.request_bluetooth_scan_permission),
                        { _, _ ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)

                            intent.data = uri
                            bluetoothScanPermissionResult.launch(intent)
                        },
                        { _, _ ->
                            Snackbar.make(findViewById(android.R.id.content), getString(R.string.request_bluetooth_scan_permission), 1500).show()
                        }
                    )
                }

                Manifest.permission.BLUETOOTH_ADVERTISE -> {
                    println("xxx BLUETOOTH_ADVERTISE : ${grantResults[index]}")
                }
            }

        }
    }

    private val locationPermissionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

    }

    private val bluetoothPermissionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

    }

    private val bluetoothScanPermissionResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {

    }

}