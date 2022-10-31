package com.eriksargsyan.eventplanner.data.database

import androidx.room.*
import com.eriksargsyan.eventplanner.data.model.database.EventDB
import com.eriksargsyan.eventplanner.util.Constants.ID
import com.eriksargsyan.eventplanner.util.Constants.TABLE_NAME

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(eventDB: EventDB)

    @Update
    suspend fun updateEvent(eventDB: EventDB)

    @Query("SELECT * FROM $TABLE_NAME")
    suspend fun getEvents(): List<EventDB>

    @Query("SELECT * FROM $TABLE_NAME WHERE $ID = :id")
    suspend fun getEventById(id: Int): EventDB

    @Query("DELETE FROM $TABLE_NAME WHERE $ID = :id")
    suspend fun deleteEventById(id: Int)


}