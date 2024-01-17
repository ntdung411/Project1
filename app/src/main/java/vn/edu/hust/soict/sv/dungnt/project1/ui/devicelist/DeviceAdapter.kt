package vn.edu.hust.soict.sv.dungnt.project1.ui.devicelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.edu.hust.soict.sv.dungnt.project1.data.model.DeviceModel
import vn.edu.hust.soict.sv.dungnt.project1.databinding.ItemDeviceBinding

class DeviceAdapter(
    private val deviceList: List<DeviceModel>,
    private val onDeviceClickListener: OnDeviceClickListener
) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemDeviceBinding.inflate(layoutInflater, parent, false)
        return DeviceViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = deviceList[position]
        holder.bind(device)
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

    interface OnDeviceClickListener {
        fun onDeviceClicked(device: DeviceModel)
    }

    inner class DeviceViewHolder(private val binding: ItemDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(device: DeviceModel) {
            binding.device = device
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onDeviceClickListener.onDeviceClicked(device)
            }
        }
    }
}
