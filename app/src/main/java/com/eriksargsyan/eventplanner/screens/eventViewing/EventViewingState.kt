package com.eriksargsyan.eventplanner.screens.eventViewing

import com.eriksargsyan.eventplanner.data.model.domain.Event

sealed interface EventViewingState {

    object Loading : EventViewingState
    data class Success(val event: Event) : EventViewingState
    object Delete: EventViewingState
    data class Error(val errorType: Int) : EventViewingState

}