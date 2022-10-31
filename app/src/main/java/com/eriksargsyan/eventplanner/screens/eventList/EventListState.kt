package com.eriksargsyan.eventplanner.screens.eventList

import com.eriksargsyan.eventplanner.data.model.domain.Event

sealed interface EventListState {

    object Loading : EventListState
    data class Success(val eventList: List<Event>) : EventListState
    data class Error(val errorType: Int) : EventListState

}