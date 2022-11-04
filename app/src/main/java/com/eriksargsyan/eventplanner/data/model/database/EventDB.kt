package com.eriksargsyan.eventplanner.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.eriksargsyan.eventplanner.data.model.domain.EventStatus
import com.eriksargsyan.eventplanner.data.model.domain.Weather
import com.eriksargsyan.eventplanner.util.Constants.TABLE_NAME
import java.util.*

@Entity(tableName = TABLE_NAME)
data class EventDB(

    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "event_name")
    val eventName: String,

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "city_name")
    val cityName: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "address_line")
    val addressLine: String,

    @ColumnInfo(name = "event_description")
    val description: String,

    @ColumnInfo(name = "event_country")
    val country: String,

    @ColumnInfo(name = "event_status")
    val status: EventStatus,

    @ColumnInfo(name = "event_weather")
    val weather: Weather,

    )

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}

class StatusConverter {
    @TypeConverter
    fun toStatus(status: Int): EventStatus {
        return EventStatus.fromStatus(status)
    }

    @TypeConverter
    fun fromStatus(eventStatus: EventStatus): Int {
        return eventStatus.status
    }
}

class WeatherConverter {
    @TypeConverter
    fun toWeather(weather: String): Weather {
        val components = weather.split(",")
        return Weather(
            weatherTemp = components[0],
            weatherIcon = components[1],
            date = components[2]
        )
    }

    @TypeConverter
    fun fromWeather(weather: Weather): String {
        return "${weather.weatherTemp},${weather.weatherIcon},${weather.date}"
    }
}

