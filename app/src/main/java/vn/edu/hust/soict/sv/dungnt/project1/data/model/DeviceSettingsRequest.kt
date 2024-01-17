package vn.edu.hust.soict.sv.dungnt.project1.data.model

data class DeviceSettingsRequest(
    val farmID: String,
    val name: String,
    val delayTime: Int,
    val resolution: Int

)