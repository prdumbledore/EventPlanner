package com.eriksargsyan.eventplanner.data.model.domain

import java.util.*


data class Event(

    val id: Int,

    val eventName: String,

    val date: Date,

    val cityName: String,

    val latitude: Double,

    val longitude: Double,

    val addressLine: String,

    val description: String,

    ) {
}