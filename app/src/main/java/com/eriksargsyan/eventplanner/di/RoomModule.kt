package com.eriksargsyan.eventplanner.di


import android.content.Context
import androidx.room.Room
import com.eriksargsyan.eventplanner.data.database.EventDao
import com.eriksargsyan.eventplanner.data.database.EventDatabase
import com.eriksargsyan.eventplanner.util.Constants.DATABASE_NAME
import com.eriksargsyan.eventplanner.util.mappers.DatabaseMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RoomModule {

    @Provides
    @Singleton
    fun provideUserDb(context: Context): EventDatabase {
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(eventDatabase: EventDatabase): EventDao {
        return eventDatabase.EventDao()
    }

    @Provides
    @Singleton
    fun provideDatabaseMapper(): DatabaseMapper = DatabaseMapper()
}