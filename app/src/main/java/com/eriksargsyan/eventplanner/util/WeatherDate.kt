package com.eriksargsyan.eventplanner.util


import com.eriksargsyan.eventplanner.data.model.domain.Weather
import java.util.*

object WeatherDate {

    fun getWeatherPosition(list: List<Weather>, date: Date): Weather {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        list.forEachIndexed { index, weather ->
            val time = weather.date.split(" ")[1].split(":")[0]
            val ymd = weather.date.split(" ")[0].split("-").map { it.toInt() }
            if (year == ymd[0] && month + 1 == ymd[1] && day == ymd[2]) {

                return when(time) {
                    "12", "15", "18", "21"-> list[index]
                    else -> Weather()
                }
            }
        }

        return Weather()
    }
}
