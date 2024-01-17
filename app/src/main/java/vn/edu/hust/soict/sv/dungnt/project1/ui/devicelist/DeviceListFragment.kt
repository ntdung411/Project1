package vn.edu.hust.soict.sv.dungnt.project1.ui.devicelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.edu.hust.soict.sv.dungnt.project1.R
import vn.edu.hust.soict.sv.dungnt.project1.data.model.DeviceModel
import vn.edu.hust.soict.sv.dungnt.project1.databinding.FragmentDeviceListBinding

class DeviceListFragment : Fragment(), DeviceAdapter.OnDeviceClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DeviceAdapter
    private var _binding: FragmentDeviceListBinding? = null
    private val devices: MutableList<DeviceModel> = mutableListOf()
    private val binding get() = _binding!!
    private lateinit var userID: String
    private lateinit var farmID: String
    private lateinit var viewModel: DeviceListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userID = "6584d33a50ac6ac7eb2f7abf"
        farmID = "658a46cd98d11056e15607d8"
        _binding = FragmentDeviceListBinding.inflate(inflater, container, false)
        val view = _binding?.root

        binding.farmID = farmID

        binding.recyclerViewDeviceList.layoutManager = LinearLayoutManager(requireContext())
        adapter = DeviceAdapter(devices, this)
        binding.recyclerViewDeviceList.adapter = adapter
        recyclerView = binding.recyclerViewDeviceList

        viewModel = ViewModelProvider(this)[DeviceListViewModel::class.java]
        viewModel.deviceList.observe(viewLifecycleOwner) { devicesFromApi ->
            updateDeviceList(devicesFromApi)
        }
        viewModel.fetchDataForFarm(farmID)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateDeviceList(devicesFromApi: List<DeviceModel>) {
        devices.clear()
        devices.addAll(devicesFromApi)
        adapter.notifyDataSetChanged()
    }

    override fun onDeviceClicked(device: DeviceModel) {

        val bundle = Bundle().apply {
            putString("deviceID", device.deviceID)
            putString("farmID", farmID)
            putString("deviceName", device.deviceName)
            putInt("delayTime", device.delayTime)
            putInt("resolution", device.resolution)

        }

        findNavController().navigate(
            R.id.action_deviceListFragment_to_imageViewFragment,
            bundle
        )
    }
}
