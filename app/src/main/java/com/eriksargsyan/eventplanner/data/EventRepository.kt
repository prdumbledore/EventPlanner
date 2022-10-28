package com.eriksargsyan.eventplanner.data


import com.eriksargsyan.eventplanner.util.Constants.API_KEY
import javax.inject.Inject


interface EventRepository {
    suspend fun getGeolocation(cityName: String)
}

class EventRepositoryImpl @Inject constructor(
    private val apiEvent: EventAPI
) : EventRepository {
    //TODO
    override suspend fun getGeolocation(cityName: String) {
        apiEvent.getGeoPosition(cityName = cityName, limit = 5, apiKey = API_KEY)
    }
}