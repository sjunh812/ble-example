package org.sjhstudio.ble

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.sjhstudio.ble.BleApplication.Companion.requestPermission
import org.sjhstudio.ble.bluetooth.BleScanResultListener
import org.sjhstudio.ble.bluetooth.BleUtils
import org.sjhstudio.ble.databinding.ActivityMainBinding
import org.sjhstudio.ble.util.Utils

class MainActivity : BaseActivity(), BleScanResultListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.toolbar.apply {
            title = "BLE"
            setTitleTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
        }

        requestPermission(this)
        testSingleton()

        binding.scanBtn.setOnClickListener { scan() }
    }

    fun testSingleton() {
        val singleton1 = BleUtils.getInstance(this)
        val singleton2 = BleUtils.getInstance(this)

        println("xxx singleton1 : $singleton1")
        println("xxx singleton2 : $singleton2")
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    fun scan() {
        binding.scanBtn.isEnabled = false
        if(BleUtils.getInstance(this).checkBluetoothEnabled()) {
//            Snackbar.make(binding.toolbar, R.string.bluetooth_on, 1000).show()
            if(Utils.checkPermissionGranted(this, Manifest.permission.BLUETOOTH_SCAN)) {
                BleUtils.getInstance(this).apply {
                    setScanResultListener(this@MainActivity)
                    scanBle(true, 7000)
                }
            }
        } else {
            Snackbar.make(binding.toolbar, R.string.bluetooth_off, 1000).show()
            if(Utils.checkPermissionGranted(this, Manifest.permission.BLUETOOTH_CONNECT)) {
                enableBluetoothResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }
    }

    private val enableBluetoothResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { ar ->
        if(ar.resultCode == RESULT_OK) {
            println("xxx enableBluetoothResult success!!")
        } else {
            println("xxx enableBluetoothResult failed..")
            Snackbar.make(binding.toolbar, R.string.request_bluetooth_on, 1500).show()
        }
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {

    }

    override fun onScanResult(result: ScanResult?) {
        if(Utils.checkPermissionGranted(this, Manifest.permission.BLUETOOTH_CONNECT)) {

        }
    }

    override fun onScanFailed(errorCode: Int) {

    }

    override fun onScanFinished()  {
        launch {
            binding.scanBtn.isEnabled = true
            println("xxx ${BleUtils.getInstance(this@MainActivity).bleList}")
        }
    }

}