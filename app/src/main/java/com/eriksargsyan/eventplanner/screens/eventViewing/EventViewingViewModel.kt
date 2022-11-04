package com.eriksargsyan.eventplanner.screens.eventViewing


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eriksargsyan.eventplanner.data.EventRepository
import com.eriksargsyan.eventplanner.data.model.domain.Event
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class EventViewingViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EventViewingState>(EventViewingState.Loading)
    val state: StateFlow<EventViewingState> = _state

    fun fetchEventDetails(id: Int) {
        viewModelScope.launch() {
            _state.value = EventViewingState.Success(eventRepository.getEvent(id))
        }
    }

    fun deleteEvent(id: Int) {
        viewModelScope.launch {
            eventRepository.deleteEvent(id)
            _state.value = EventViewingState.Delete
        }

    }

    fun setNewEventStatus(event: Event) {
        viewModelScope.launch {
            EventViewingState.Success(eventRepository.saveEvent(event))
        }

    }

    class EventViewingViewModelFactory @AssistedInject constructor(
        private val eventRepository: EventRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            EventViewingViewModel(eventRepository) as T

        @AssistedFactory
        interface Factory {

            fun create(): EventViewingViewModelFactory
        }
    }
}