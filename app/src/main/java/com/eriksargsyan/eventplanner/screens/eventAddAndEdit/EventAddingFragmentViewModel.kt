package com.eriksargsyan.eventplanner.screens.eventAddAndEdit

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eriksargsyan.eventplanner.data.EventRepository
import com.eriksargsyan.eventplanner.data.model.domain.CityName
import com.eriksargsyan.eventplanner.data.model.domain.Event
import com.eriksargsyan.eventplanner.util.ErrorConstants.NO_NETWORK_CONNECTION
import com.eriksargsyan.eventplanner.util.ErrorConstants.OTHER_ERROR
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class EventAddingFragmentViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchListState>(SearchListState.Loading)
    val state: StateFlow<SearchListState> = _state


    fun fetchEventGeo(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = if (eventRepository.hasNetworkAccess()) {
                try {
                    if (cityName.isEmpty()) SearchListState.Searching(emptyList())
                    else SearchListState.Searching(eventRepository.getGeolocation(cityName = cityName))
                } catch (e: IllegalStateException) {
                    SearchListState.Error(OTHER_ERROR)
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
        addressLine: String,
        eventDescription: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.saveEvent(Event(
                id = eventId,
                eventName = eventName,
                date = eventDate,
                cityName = cityName.name,
                latitude = cityName.latitude,
                longitude = cityName.longitude,
                addressLine = addressLine,
                description = eventDescription,
            ))

            _state.value = SearchListState.Result
        }
    }

    class EventAddingFragmentViewModelFactory @AssistedInject constructor(
        private val eventRepository: EventRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            EventAddingFragmentViewModel(eventRepository) as T

        @AssistedFactory
        interface Factory {

            fun create(): EventAddingFragmentViewModelFactory
        }
    }
}

