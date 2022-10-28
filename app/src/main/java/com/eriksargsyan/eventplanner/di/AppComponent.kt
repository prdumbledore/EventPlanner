package com.eriksargsyan.eventplanner.di

import com.eriksargsyan.eventplanner.screens.eventAdding.EventAddingFragment
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(fragment: EventAddingFragment)
}