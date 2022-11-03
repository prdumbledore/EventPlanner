package com.eriksargsyan.eventplanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eriksargsyan.eventplanner.data.model.database.DateConverter
import com.eriksargsyan.eventplanner.data.model.database.EventDB
import com.eriksargsyan.eventplanner.data.model.database.StatusConverter

@Database(entities = [EventDB::class], version = 1)
@TypeConverters(DateConverter::class, StatusConverter::class)
abstract class EventDatabase: RoomDatabase() {

    abstract fun EventDao(): EventDao

}