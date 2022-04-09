package org.sjhstudio.ble.viewmodel

import android.Manifest
import android.app.Application
import android.bluetooth.le.ScanResult
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sjhstudio.ble.bluetooth.BleScanResultListener
import org.sjhstudio.ble.bluetooth.BleUtils
import org.sjhstudio.ble.util.Utils
import org.sjhstudio.ble.util.Val

class MainViewModel(private val app: Application): AndroidViewModel(app), BleScanResultListener {

    private var _scanResultLiveData = MutableLiveData<ArrayList<ScanResult>>()
    val scanResultLiveData: LiveData<ArrayList<ScanResult>>
        get() = _scanResultLiveData

    private var _resultLiveData = MutableLiveData<Map<String,String>>()
    val resultLiveData: LiveData<Map<String,String>>
        get() = _resultLiveData

    init {
        _scanResultLiveData.value = arrayListOf()
        _resultLiveData.value = mapOf()
        BleUtils.getInstance(app).setScanResultListener(this)
    }

    fun clearScanResult() {
        _scanResultLiveData.value = arrayListOf()
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    fun scanBle() {
        if(Utils.checkPermissionGranted(app, Manifest.permission.BLUETOOTH_SCAN)) {
            BleUtils.getInstance(app).scanBle(true, 7000)
        }
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        println("xxx onBatchScanResults()")
    }

    override fun onScanResult(result: ScanResult?) {
        println("xxx onScanResult()")
        try {
            result?.let { r ->
                _scanResultLiveData.value = _scanResultLiveData.value?.apply { add(r) }
            }
        } catch(e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onScanFailed(errorCode: Int) {
        viewModelScope.launch {
            println("xxx onScanFailed()")
            _resultLiveData.value = mapOf(Pair(Val.SCAN_FAILED, "스캔 실패"))
        }
    }

    override fun onScanFinished() {
        viewModelScope.launch {
            println("xxx onScanFinished()")
            _resultLiveData.value = mapOf(Pair(Val.SCAN_FINISHED, "스캔 끝"))
        }
    }

}