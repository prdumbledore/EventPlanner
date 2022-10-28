package com.eriksargsyan.eventplanner.data.model.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class CityNameNet(

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("lat")
    val latitude: Double,

    @Expose
    @SerializedName("lon")
    val longitude: Double,

    @Expose
    @SerializedName("country")
    val country: String
)