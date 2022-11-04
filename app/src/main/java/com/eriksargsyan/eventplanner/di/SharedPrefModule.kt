package com.eriksargsyan.eventplanner.di

import android.content.Context
import com.eriksargsyan.eventplanner.data.database.WeatherSettings
import dagger.Module
import dagger.Provides

@Module
class SharedPrefModule {

    @Provides
    fun provideSharedPref(context: Context): WeatherSettings =
        WeatherSettings(context)
}