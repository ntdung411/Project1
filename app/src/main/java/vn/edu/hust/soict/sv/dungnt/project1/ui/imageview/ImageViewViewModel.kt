package vn.edu.hust.soict.sv.dungnt.project1.ui.imageview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageViewViewModel : ViewModel() {
    val deviceName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val delayTime: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val resolution: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
}