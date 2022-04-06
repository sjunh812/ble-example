package org.sjhstudio.ble.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent

class BleUtils private constructor(){

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: BleUtils? = null
        @SuppressLint("StaticFieldLeak")
        private lateinit var mContext: Context

        private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
            val bluetoothManager = mContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothManager.adapter
        }

        fun getInstance(context: Context): BleUtils {
            return instance ?: synchronized(this) {
                BleUtils().also {
                    mContext = context.applicationContext
                    instance = it
                }
            }
        }

    }

    fun checkBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

}