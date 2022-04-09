package org.sjhstudio.ble.adapter

import android.Manifest
import android.bluetooth.le.ScanResult
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.recyclerview.widget.RecyclerView
import org.sjhstudio.ble.R
import org.sjhstudio.ble.databinding.ItemScanListBinding
import org.sjhstudio.ble.util.Utils

class ScanResultAdapter(context: Context): RecyclerView.Adapter<ScanResultAdapter.ViewHolder>() {

    var mContext = context
    var items: ArrayList<ScanResult> = arrayListOf()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var binding = ItemScanListBinding.bind(itemView)

        @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
        fun setBind(data: ScanResult) {
            if(Utils.checkPermissionGranted(mContext, Manifest.permission.BLUETOOTH_CONNECT)) {
                binding.deviceNameTv.text = data.device.name
                binding.container.setOnClickListener {

                }
            }
        }

    }

    fun addItem(item: ScanResult) {
        items.add(item)
        notifyItemChanged(items.lastIndex)
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_list, parent, false)

        return ViewHolder(itemView)
    }

    @RequiresPermission(value = "android.permission.BLUETOOTH_CONNECT")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}