package com.eriksargsyan.eventplanner.util

import java.util.*

object EventTxtTransform {

    fun dateToDMY(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return "${
            calendar.get(Calendar.DAY_OF_MONTH)
        }/${
            calendar.get(Calendar.MONTH)
        }/${
            calendar.get(Calendar.YEAR)
        }"

    }
}