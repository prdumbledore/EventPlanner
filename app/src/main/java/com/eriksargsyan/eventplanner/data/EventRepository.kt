package com.eriksargsyan.eventplanner.data


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.eriksargsyan.eventplanner.data.database.EventDao
import com.eriksargsyan.eventplanner.data.model.domain.CityName
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.data.network.EventAPI
import com.eriksargsyan.eventplanner.util.Constants.API_KEY
import com.eriksargsyan.eventplanner.util.DatabaseMapper
import com.eriksargsyan.eventplanner.util.IO
import com.eriksargsyan.eventplanner.util.NetworkCityNameMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface EventRepository {
    suspend fun getGeolocation(cityName: String): List<CityName>
    suspend fun saveEvent(event: Event)
    suspend fun getAllEvents(): List<Event>
    suspend fun getEvent(id: Int): Event
    suspend fun deleteEvent(id: Int)
    suspend fun hasNetworkAccess(): Boolean
}

class EventRepositoryImpl @Inject constructor(
    private val apiEvent: EventAPI,
    private val eventDao: EventDao,
    private val networkCityNameMapper: NetworkCityNameMapper,
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

    override suspend fun saveEvent(event: Event) {
        withContext(dispatcherIO) {
            val eventDB = databaseMapper.domainToEntityMap(event)
            eventDao.insertEvent(eventDB)
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
            eventDao.deleteEventById(id)
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