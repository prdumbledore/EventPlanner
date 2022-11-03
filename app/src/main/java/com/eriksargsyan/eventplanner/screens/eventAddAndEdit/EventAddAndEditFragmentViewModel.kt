package com.eriksargsyan.eventplanner.screens.eventAddAndEdit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eriksargsyan.eventplanner.data.EventRepository
import com.eriksargsyan.eventplanner.data.model.domain.CityName
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.data.model.domain.EventStatus
import com.eriksargsyan.eventplanner.screens.eventList.EventListState
import com.eriksargsyan.eventplanner.util.ErrorConstants.NO_NETWORK_CONNECTION
import com.eriksargsyan.eventplanner.util.ErrorConstants.STATE_EXCEPTION
import com.eriksargsyan.eventplanner.util.ErrorConstants.UNKNOWN_HOST_EXCEPTION
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import java.util.*

class EventAddAndEditFragmentViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchListState>(SearchListState.Loading)
    val state: StateFlow<SearchListState> = _state


    fun fetchEventGeo(cityName: String) {
        viewModelScope.launch {
            _state.value = if (eventRepository.hasNetworkAccess()) {
                try {
                    if (cityName.isEmpty()) SearchListState.Searching(emptyList())
                    else SearchListState.Searching(eventRepository.getGeolocation(cityName = cityName))
                } catch (e: IllegalStateException) {
                    SearchListState.Error(STATE_EXCEPTION)
                } catch (e: UnknownHostException) {
                    e.printStackTrace()
                    SearchListState.Error(UNKNOWN_HOST_EXCEPTION)
                }
            } else {
                SearchListState.Error(NO_NETWORK_CONNECTION)
            }
        }

    }

    fun saveResult(
        eventId: Int,
        eventName: String,
        eventDate: Date,
        cityName: CityName,
        eventAddressLine: String,
        eventDescription: String,
        eventStatus: EventStatus,
    ) {
        viewModelScope.launch {
            eventRepository.saveEvent(
                Event(
                    id = eventId,
                    eventName = eventName,
                    date = eventDate,
                    cityName = cityName.name,
                    latitude = cityName.latitude,
                    longitude = cityName.longitude,
                    addressLine = eventAddressLine,
                    description = eventDescription,
                    country = cityName.country,
                    status = eventStatus
                )
            )
            _state.value = SearchListState.Result
        }
    }

    fun fetchField(id: Int) {
        viewModelScope.launch {
            _state.value = try {
                SearchListState.Editing(eventRepository.getEvent(id))
            } catch (e: IllegalStateException) {
                SearchListState.Error(STATE_EXCEPTION)
            }
        }
    }

    fun setLoadingState() {
        _state.value = SearchListState.Loading
    }

    class EventAddingFragmentViewModelFactory @AssistedInject constructor(
        private val eventRepository: EventRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            EventAddAndEditFragmentViewModel(eventRepository) as T

        @AssistedFactory
        interface Factory {

            fun create(): EventAddingFragmentViewModelFactory
        }
    }
}

