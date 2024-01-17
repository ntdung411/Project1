package vn.edu.hust.soict.sv.dungnt.project1.data.model

import com.squareup.moshi.Json

data class DeviceModel(
    @Json(name = "_id")
    var deviceID: String = "",
    @Json(name = "name")
    var deviceName: String = "",
    @Json(name = "delayTime")
    var delayTime: Int = 0,
    @Json(name = "resolution")
    var resolution: Int = 0
)