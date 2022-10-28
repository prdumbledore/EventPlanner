package com.eriksargsyan.eventplanner.screens.eventAdding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eriksargsyan.eventplanner.data.EventRepository
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EventAddingFragmentViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    fun fetchEventGeo() {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.getGeolocation("Moscow")
        }
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

