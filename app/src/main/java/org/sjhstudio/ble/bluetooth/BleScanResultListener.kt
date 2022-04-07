package org.sjhstudio.ble.bluetooth

import android.bluetooth.le.ScanResult

interface BleScanResultListener {

    fun onBatchScanResults(results: MutableList<ScanResult>?)

    fun onScanResult(result: ScanResult?)

    fun onScanFailed(errorCode: Int)

    fun onScanFinished()

}