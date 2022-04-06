package org.sjhstudio.ble.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.util.TypedValue
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.sjhstudio.ble.R
import kotlin.math.roundToInt

class Utils {

    companion object {

        private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

        fun checkBleFeature(context: Context) {
            context.packageManager.takeIf {
                it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
            }?.also {
                Toast.makeText(context, R.string.ble_not_supported, Toast.LENGTH_SHORT).show()
            }
        }

        fun checkPermissionGranted(context: Context, permission: String): Boolean {
            return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }

        fun showYesOrNoDialog(
            context: Context,
            title: String,
            message: String,
            positiveListener: DialogInterface.OnClickListener,
            negativeListener: DialogInterface.OnClickListener
        ) {
            val dialog = AlertDialog.Builder(
                context,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, positiveListener)
                .setNegativeButton(R.string.no, negativeListener)
                .create()
            dialog.apply {
                window?.setGravity(Gravity.CENTER)
                show()
                getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                    setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                    setTextColor(ContextCompat.getColor(context, R.color.purple_500))
                }
                getButton(AlertDialog.BUTTON_POSITIVE).apply {
                    setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                    setTextColor(ContextCompat.getColor(context, R.color.purple_500))
                }
            }
        }

        fun dpToPx(context: Context, dp: Int): Float {
            return dp*context.resources.displayMetrics.density
        }

        fun pxToDp(context: Context, px: Float): Int {
            return (px/context.resources.displayMetrics.density).toInt()
        }

    }

}