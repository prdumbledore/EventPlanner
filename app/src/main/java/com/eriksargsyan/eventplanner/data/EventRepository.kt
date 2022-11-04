package com.eriksargsyan.eventplanner.data


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.eriksargsyan.eventplanner.data.database.EventDao
import com.eriksargsyan.eventplanner.data.model.domain.CityName
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.data.model.domain.Weather
import com.eriksargsyan.eventplanner.data.network.EventAPI
import com.eriksargsyan.eventplanner.util.Constants.API_KEY
import com.eriksargsyan.eventplanner.util.mappers.DatabaseMapper
import com.eriksargsyan.eventplanner.util.IO
import com.eriksargsyan.eventplanner.util.WeatherDate.getWeatherPosition
import com.eriksargsyan.eventplanner.util.mappers.NetworkCityNameMapper
import com.eriksargsyan.eventplanner.util.mappers.NetworkWeatherMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface EventRepository {
    suspend fun getGeolocation(cityName: String): List<CityName>
    suspend fun saveEvent(event: Event): Event
    suspend fun getAllEvents(): List<Event>
    suspend fun getEvent(id: Int): Event
    suspend fun deleteEvent(id: Int)
    suspend fun hasNetworkAccess(): Boolean
}

class EventRepositoryImpl @Inject constructor(
    private val apiEvent: EventAPI,
    private val eventDao: EventDao,
    private val networkCityNameMapper: NetworkCityNameMapper,
    private val networkWeatherMapper: NetworkWeatherMapper,
    private val databaseMapper: DatabaseMapper,
    private val context: Context,
    @IO private val dispatcherIO: CoroutineDispatcher
) : EventRepository {

    override suspend fun getGeolocation(cityName: String): List<CityName> {
        return withContext(dispatcherIO) {
            val cityNameNetList =
                apiEvent.getGeoPosition(cityName = cityName, limit = 5, apiKey = API_KEY)
            return@withContext networkCityNameMapper.entityToDomainMapList(cityNameNetList)
        }
    }

    override suspend fun saveEvent(event: Event): Event {
        return withContext(dispatcherIO) {
            val weatherList = networkWeatherMapper.entityToDomainMap(
                apiEvent.getWeather(
                    latitude = event.latitude,
                    longitude = event.longitude,
                    apiKey = API_KEY
                )
            )

            val weather = getWeatherPosition(weatherList, event.date)
            val eventDB = databaseMapper.domainToEntityMap(event.copy(weather = weather))
            launch { eventDao.insertEvent(eventDB) }
            return@withContext event.copy(weather = weather)
        }
    }

    override suspend fun getAllEvents(): List<Event> {
        return withContext(dispatcherIO) {
            return@withContext databaseMapper.entityToDomainMapList(eventDao.getEvents())
        }
    }

    override suspend fun getEvent(id: Int): Event {
        return withContext(dispatcherIO) {
            return@withContext databaseMapper.entityToDomainMap(eventDao.getEventById(id))
        }
    }

    override suspend fun deleteEvent(id: Int) {
        withContext(dispatcherIO) {
            launch { eventDao.deleteEventById(id) }
        }
    }


    override suspend fun hasNetworkAccess(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


    //TODO
}