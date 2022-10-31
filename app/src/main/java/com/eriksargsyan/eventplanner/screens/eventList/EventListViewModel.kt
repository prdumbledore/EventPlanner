package com.eriksargsyan.eventplanner.screens.eventList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eriksargsyan.eventplanner.data.EventRepository
import com.eriksargsyan.eventplanner.util.ErrorConstants.STATE_EXCEPTION
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventListViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow<EventListState>(EventListState.Loading)
    val state: StateFlow<EventListState> = _state

    fun fetchEvents() {

        viewModelScope.launch(Dispatchers.IO) {
            _state.value = try {
                EventListState.Success(eventRepository.getAllEvents())
            } catch (e: IllegalStateException) {
                EventListState.Error(STATE_EXCEPTION)
            }
        }

    }

    fun setLoadingState() {
        _state.value = EventListState.Loading
    }

    class EventListViewModelFactory @AssistedInject constructor(
        private val eventRepository: EventRepository
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            EventListViewModel(eventRepository) as T

        @AssistedFactory
        interface Factory {

            fun create(): EventListViewModelFactory
        }
    }
}