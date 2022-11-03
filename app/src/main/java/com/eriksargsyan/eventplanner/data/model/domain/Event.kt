package com.eriksargsyan.eventplanner.data.model.domain

import com.eriksargsyan.eventplanner.R
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

    val country: String,

    val status: EventStatus,
    )

enum class EventStatus(val id: Int, val status: Int) {
    COMING_CHIP(R.id.coming_chip, 0),
    VISITED_CHIP(R.id.visited_chip, 1),
    MISSED_CHIP(R.id.missed_chip, 2);

    companion object {
        fun fromId(id: Int): EventStatus {
            for (chip in values()) {
                if (id == chip.id) {
                    return chip
                }
            }
            throw IllegalArgumentException("Not implemented chip")
        }

        fun fromStatus(status: Int): EventStatus {
            for (chip in values()) {
                if (status == chip.status) {
                    return chip
                }
            }
            throw IllegalArgumentException("Not implemented chip")
        }
    }

}