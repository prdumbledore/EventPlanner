package com.eriksargsyan.eventplanner.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eriksargsyan.eventplanner.data.model.database.DateConverter
import com.eriksargsyan.eventplanner.data.model.database.EventDB

@Database(entities = [EventDB::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class EventDatabase: RoomDatabase() {

    abstract fun EventDao(): EventDao

    companion object {
        const val DATABASE_NAME = "event_db"
    }
}