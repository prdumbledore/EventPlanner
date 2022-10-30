package com.eriksargsyan.eventplanner.data.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.eriksargsyan.eventplanner.util.Constants.TABLE_NAME
import java.util.*

@Entity(tableName = TABLE_NAME)
data class EventDB(

    @PrimaryKey(autoGenerate = false)
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

