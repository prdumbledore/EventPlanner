package com.eriksargsyan.eventplanner.data

import com.eriksargsyan.eventplanner.data.model.network.CityNameNet
import retrofit2.http.GET
import retrofit2.http.Query

interface EventAPI {

    @GET("geo/1.0/direct?")
    suspend fun getGeoPosition(
        @Query("q") cityName: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String
    ): List<CityNameNet>
}