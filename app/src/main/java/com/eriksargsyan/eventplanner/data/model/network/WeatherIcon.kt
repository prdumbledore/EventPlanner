package com.eriksargsyan.eventplanner.data.model.network

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherIcon(val url: String, val iconId: String) : Parcelable {

    companion object {
        fun getWeatherIcons(): Array<WeatherIcon> {
            return arrayOf(
                WeatherIcon("http://openweathermap.org/img/wn/01d@2x.png", "01"),
                WeatherIcon("http://openweathermap.org/img/wn/02d@2x.png", "02"),
                WeatherIcon("http://openweathermap.org/img/wn/03d@2x.png", "03"),
                WeatherIcon("http://openweathermap.org/img/wn/04d@2x.png", "04"),
                WeatherIcon("http://openweathermap.org/img/wn/09d@2x.png", "09"),
                WeatherIcon("http://openweathermap.org/img/wn/10d@2x.png", "10"),
                WeatherIcon("http://openweathermap.org/img/wn/11d@2x.png", "11"),
                WeatherIcon("http://openweathermap.org/img/wn/13d@2x.png", "13"),
                WeatherIcon("http://openweathermap.org/img/wn/50d@2x.png", "50"),)
        }
    }
}