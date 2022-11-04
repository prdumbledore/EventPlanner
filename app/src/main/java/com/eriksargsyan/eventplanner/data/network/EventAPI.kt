package com.eriksargsyan.eventplanner.data.network

import com.eriksargsyan.eventplanner.data.model.network.CityNameNet
import com.eriksargsyan.eventplanner.data.model.network.WeatherListNet
import retrofit2.http.GET
import retrofit2.http.Query

interface EventAPI {

    @GET("geo/1.0/direct?")
    suspend fun getGeoPosition(
        @Query("q") cityName: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): List<CityNameNet>

    @GET("data/2.5/forecast?")
    suspend fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("cnt") timeSplitCount: Int = 40,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherListNet
}