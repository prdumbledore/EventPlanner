package com.eriksargsyan.eventplanner.screens.eventList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eriksargsyan.eventplanner.data.EventRepository
import com.eriksargsyan.eventplanner.data.database.WeatherSettings
import com.eriksargsyan.eventplanner.util.ErrorConstants.NO_NETWORK_CONNECTION
import com.eriksargsyan.eventplanner.util.ErrorConstants.STATE_EXCEPTION
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class EventListViewModel(
    private val eventRepository: EventRepository,
    private val weatherSettings: WeatherSettings,
) : ViewModel() {

    private val _state = MutableStateFlow<EventListState>(EventListState.Loading)
    val state: StateFlow<EventListState> = _state

    init {
        weatherSettings.setExistFlag(false)
    }


    fun fetchEvents() {
        viewModelScope.launch {
            _state.value = try {
                if (!weatherSettings.isExist()) {
                    weatherSettings.setExistFlag(true)
                    EventListState.Success(eventRepository.getAllEventsWithWeatherUpdate())
                } else EventListState.Success(eventRepository.getAllEvents())
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                EventListState.Error(NO_NETWORK_CONNECTION)
            }
        }

    }

    fun setLoadingState() {
        _state.value = EventListState.Loading
    }

    fun clearSettings() {
        weatherSettings.setExistFlag(false)
    }

    class EventListViewModelFactory @AssistedInject constructor(
        private val eventRepository: EventRepository,
        private val weatherSettings: WeatherSettings
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            EventListViewModel(eventRepository, weatherSettings) as T

        @AssistedFactory
        interface Factory {

            fun create(): EventListViewModelFactory
        }
    }
}