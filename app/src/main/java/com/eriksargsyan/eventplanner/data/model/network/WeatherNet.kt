package com.eriksargsyan.eventplanner.data.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherListNet(
    @Expose
    @SerializedName("list")
    val weatherItem: List<WeatherNet>,
)

data class WeatherNet(

    @Expose
    @SerializedName("main")
    val weatherTemp: WeatherTempNet,

    @Expose
    @SerializedName("weather")
    val weatherIcon: List<WeatherDetailsNet>,

    @Expose
    @SerializedName("dt_txt")
    val date: String,

    )

data class WeatherTempNet(

    @Expose
    @SerializedName("temp")
    val temp: Double

)

data class WeatherDetailsNet(

    @Expose
    @SerializedName("icon")
    val iconId: String

)
