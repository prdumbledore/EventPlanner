package com.eriksargsyan.eventplanner.screens.eventAddAndEdit

import com.eriksargsyan.eventplanner.data.model.domain.CityName


sealed interface SearchListState {
    object Loading: SearchListState
    data class Searching(val cityList: List<CityName>): SearchListState
    object Result: SearchListState
    data class Error(val errorType: Int): SearchListState
}