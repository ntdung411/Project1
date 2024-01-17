package vn.edu.hust.soict.sv.dungnt.project1.ui.devicelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vn.edu.hust.soict.sv.dungnt.project1.data.model.DeviceModel
import vn.edu.hust.soict.sv.dungnt.project1.data.remote.Api
import java.io.IOException

class DeviceListViewModel : ViewModel() {
    private val _deviceList = MutableLiveData<List<DeviceModel>>()
    val deviceList: LiveData<List<DeviceModel>>
        get() = _deviceList

    fun fetchDataForFarm(farmId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val devices = Api.retrofitService.getDevices(farmId)
                _deviceList.postValue(devices)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("DeviceListFragment", "not Received devices from API")
            }
        }
    }
}
