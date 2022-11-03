package com.eriksargsyan.eventplanner.screens.eventAddAndEdit

import com.eriksargsyan.eventplanner.data.model.domain.CityName
import com.eriksargsyan.eventplanner.data.model.domain.Event


sealed interface SearchListState {
    object Loading: SearchListState
    data class Searching(val cityList: List<CityName>): SearchListState
    data class Editing(val event: Event): SearchListState
    data class Result(val event: Event): SearchListState
    data class Error(val errorType: Int): SearchListState
}