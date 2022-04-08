package org.sjhstudio.ble.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.Context
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BleUtils private constructor(): ScanCallback(), CoroutineScope {

    private var isScanning = false
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var scanResultListener: BleScanResultListener? = null
    private var bleScanner: BluetoothLeScanner? = null
    var bleList: ArrayList<String> = arrayListOf()

    override val coroutineContext: CoroutineContext
        get() = Default

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: BleUtils? = null

        fun getInstance(context: Context): BleUtils {
            return instance ?: synchronized(this) {
                BleUtils().also {
                    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                    it.bluetoothAdapter = bluetoothManager.adapter
                    it.bleScanner = bluetoothManager.adapter.bluetoothLeScanner
                    instance = it
                }
            }
        }

    }

    fun setScanResultListener(listener: BleScanResultListener) {
        scanResultListener = listener
    }

    fun checkBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_SCAN")
    fun scanBle(enable: Boolean, period: Long) {
        val sec = (period/1000).toInt()
        println("xxx scanBle(${sec}s)")
        bleList.clear()
        when(enable) {
            true -> {
                launch {
                    isScanning = true
                    val scanSettings = ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .setReportDelay(0)
                        .build()
                    bleScanner?.startScan(arrayListOf<ScanFilter>(), scanSettings, this@BleUtils)

                    delay(period)

                    if(isScanning) {
                        println("xxx scanBle() 종료 : ${sec}초 경과")
                        println("xxx ${bleScanner == null}")
                        isScanning = false
                        bleScanner?.stopScan(this@BleUtils)
                        scanResultListener?.onScanFinished()
                    } else {
                        println("xxx scanBle() 종료 : 이전에 이미 스캔종료")
                    }
                }
            }

            else -> {
                isScanning = false
                bleScanner?.stopScan(this)
                scanResultListener?.onScanFinished()
            }
        }
    }


    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        super.onBatchScanResults(results)
        scanResultListener?.onBatchScanResults(results)
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        result?.device?.name?.let {
            if(!bleList.contains(it)) {
                println("xxx $it")
                bleList.add(it)
                scanResultListener?.onScanResult(result)
            }
        }
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        scanResultListener?.onScanFailed(errorCode)
    }

}