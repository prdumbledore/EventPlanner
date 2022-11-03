package com.eriksargsyan.eventplanner.di

import com.eriksargsyan.eventplanner.data.EventRepository
import com.eriksargsyan.eventplanner.data.EventRepositoryImpl
import com.eriksargsyan.eventplanner.util.Dispatchers
import com.eriksargsyan.eventplanner.util.IO
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher

@Module
interface RepositoryModule {

    @Binds
    fun bindEventRepository(eventRepositoryImpl: EventRepositoryImpl): EventRepository

}