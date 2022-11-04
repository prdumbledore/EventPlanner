package com.eriksargsyan.eventplanner.data.model.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherIcon(
    val url: String
) : Parcelable


enum class WeatherIconType(val wIconURL: WeatherIcon, val iconId: String) {

    CLEAR_SKY(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/01d@2x.png"), "01"),
    FEW_CLOUDS(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/02d@2x.png"), "02"),
    SCATTERED_CLOUDS(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/03d@2x.png"), "03"),
    BROKEN_CLOUDS(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/04d@2x.png"), "04"),
    SHOWER_RAIN(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/09d@2x.png"), "09"),
    RAIN(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/10d@2x.png"), "10"),
    THUNDERSTORM(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/11d@2x.png"), "11"),
    SNOW(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/13d@2x.png"), "13"),
    MIST(WeatherIcon("http://www.simple-catering.ru/WeatherIcons/50d@2x.png"), "50");

    companion object {

        fun fromId(iconId: String): WeatherIconType {
            for (type in values()) {
                if (iconId == type.iconId) {
                    return type
                }
            }
            throw IllegalArgumentException("Not implemented weather")
        }
    }

}
