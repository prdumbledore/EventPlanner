package com.eriksargsyan.eventplanner.di

import com.eriksargsyan.eventplanner.data.EventRepository
import com.eriksargsyan.eventplanner.data.EventRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun bindEventRepository(eventRepositoryImpl: EventRepositoryImpl): EventRepository
}