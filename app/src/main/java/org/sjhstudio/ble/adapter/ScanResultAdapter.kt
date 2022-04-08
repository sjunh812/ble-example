package org.sjhstudio.ble.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.sjhstudio.ble.R
import org.sjhstudio.ble.databinding.ItemScanListBinding

class ScanResultAdapter: RecyclerView.Adapter<ScanResultAdapter.ViewHolder>() {

    var items: ArrayList<String> = arrayListOf()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var binding = ItemScanListBinding.bind(itemView)

        fun setBind(data: String) {
            binding.deviceNameTv.text = data
            binding.container.setOnClickListener {

            }
        }

    }

    fun addItem(item: String) {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}