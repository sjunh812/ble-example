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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.sjhstudio.ble.BleApplication.Companion.requestPermission
import org.sjhstudio.ble.adapter.ScanResultAdapter
import org.sjhstudio.ble.bluetooth.BleScanResultListener
import org.sjhstudio.ble.bluetooth.BleUtils
import org.sjhstudio.ble.databinding.ActivityMainBinding
import org.sjhstudio.ble.util.BaseActivity
import org.sjhstudio.ble.util.Utils

class MainActivity : BaseActivity(), BleScanResultListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var scanResultAdapter: ScanResultAdapter

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        requestPermission(this)
        Utils.checkBleUtilsSingleton(this)
        initUI()
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    fun initUI() {
        binding.toolbar.apply {
            title = "BLE"
            setTitleTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
        }
        scanResultAdapter = ScanResultAdapter()
        binding.scanResultRv.apply {
            adapter = scanResultAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.scanBtn.setOnClickListener {
            scan()
        }
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    fun scan() {
        binding.scanBtn.isEnabled = false
        scanResultAdapter.clearItems()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            if(it == -1) {
                binding.scanBtn.isEnabled = false
                binding.scanBtn.text = getString(R.string.check_permission)
                return
            }
        }
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {

    }

    override fun onScanResult(result: ScanResult?) {
        if(Utils.checkPermissionGranted(this, Manifest.permission.BLUETOOTH_CONNECT)) {
            try {
                result?.device?.name?.let {
                    scanResultAdapter.addItem(it)
                }
            } catch(e: SecurityException) {
                e.printStackTrace()
                Snackbar.make(binding.scanResultRv, getString(R.string.request_bluetooth_permission), 1500).show()
            }
        }
    }

    override fun onScanFailed(errorCode: Int) {

    }

    override fun onScanFinished()  {
        launch {
            println("xxx onScanFinished() : 스캔완료")
            binding.scanBtn.isEnabled = true
        }
    }

}