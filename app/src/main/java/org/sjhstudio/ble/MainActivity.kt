package org.sjhstudio.ble

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.sjhstudio.ble.BleApplication.Companion.requestPermission
import org.sjhstudio.ble.adapter.ScanResultAdapter
import org.sjhstudio.ble.bluetooth.BleUtils
import org.sjhstudio.ble.databinding.ActivityMainBinding
import org.sjhstudio.ble.util.BaseActivity
import org.sjhstudio.ble.util.Utils
import org.sjhstudio.ble.util.Val
import org.sjhstudio.ble.viewmodel.MainViewModel

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainVm: MainViewModel
    private lateinit var scanResultAdapter: ScanResultAdapter

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainVm = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[MainViewModel::class.java]

        requestPermission(this)
        Utils.checkBleUtilsSingleton(this)
        initUi()
        observeScanResult()
        observeResult()
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    fun initUi() {
        binding.toolbar.apply {
            title = "BLE"
            setTitleTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
        }
        scanResultAdapter = ScanResultAdapter(this)
        binding.scanResultRv.apply {
            adapter = scanResultAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.scanBtn.setOnClickListener {
            if(BleUtils.getInstance(this).checkBluetoothEnabled()) {
                binding.scanBtn.isEnabled = false
                mainVm.clearScanResult()
                mainVm.scanBle()
            } else {
                Snackbar.make(binding.toolbar, "블루투스를 먼저 켜주세요.", 1500).show()
                enableBluetoothResult.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }
    }

    private fun observeScanResult() {
        mainVm.scanResultLiveData.observe(this) {
            println("xxx observeScanResult() : size(${it.size})")
            scanResultAdapter.items = it
            scanResultAdapter.notifyDataSetChanged()
        }
    }

    private fun observeResult() {
        mainVm.resultLiveData.observe(this) {
            println("xxx observeResult()")
            when {
                it.containsKey(Val.SCAN_FAILED) -> {
                    Snackbar.make(binding.toolbar, it[Val.SCAN_FAILED] ?: "오류 발생", 1000).show()
                }

                it.containsKey(Val.SCAN_FINISHED) -> {
                    binding.scanBtn.isEnabled = true
                    Snackbar.make(binding.toolbar, it[Val.SCAN_FINISHED] ?: "오류 발생", 1000).show()
                }
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

}