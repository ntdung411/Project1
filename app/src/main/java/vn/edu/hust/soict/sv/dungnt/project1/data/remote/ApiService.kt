package vn.edu.hust.soict.sv.dungnt.project1.data.remote

import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import vn.edu.hust.soict.sv.dungnt.project1.data.model.DeviceModel
import vn.edu.hust.soict.sv.dungnt.project1.data.model.DeviceSettingsRequest
import vn.edu.hust.soict.sv.dungnt.project1.data.model.ImageModel
import java.util.Date

private const val BASE_URL = "http://103.176.178.96:8000/api/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("camdevice/farm/{farmID}")
    suspend fun getDevices(@Path("farmID") farmID: String): List<DeviceModel>

    @GET("image/{deviceID}")
    suspend fun getListImages(@Path("deviceID") deviceID: String): List<ImageModel>

    @GET("image/{deviceID}")
    suspend fun getListImagesByTime(
        @Path("deviceID") deviceID: String,
        @Query("fromTime") fromTime: String,
        @Query("toTime") toTime: String
    ): List<ImageModel>

    @GET("image/{deviceID}")
    suspend fun getListImagesByFromTime(
        @Path("deviceID") deviceID: String,
        @Query("fromTime") fromTime: String
    ): List<ImageModel>

    @GET("image/{deviceID}")
    suspend fun getListImagesByToTime(
        @Path("deviceID") deviceID: String,
        @Query("toTime") toTime: String
    ): List<ImageModel>

    @PUT("camdevice/{deviceID}")
    suspend fun putDeviceSettings(
        @Path("deviceID") deviceID: String,
        @Body body: DeviceSettingsRequest
    )
}

object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}