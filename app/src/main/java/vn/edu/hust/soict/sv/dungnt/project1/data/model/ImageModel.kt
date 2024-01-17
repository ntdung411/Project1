package vn.edu.hust.soict.sv.dungnt.project1.data.model

import com.squareup.moshi.Json
import java.util.Date

data class ImageModel(
    @Json(name = "_id")
    var imageID: String = "",
    @Json(name = "deviceID")
    var deviceID: String = "",
    @Json(name = "createdAt")
    var createdAt: Date
)