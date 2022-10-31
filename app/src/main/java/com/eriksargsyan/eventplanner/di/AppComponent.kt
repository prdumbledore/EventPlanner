package com.eriksargsyan.eventplanner.di

import android.content.Context
import com.eriksargsyan.eventplanner.screens.eventAddAndEdit.EventAddAndEditFragment
import com.eriksargsyan.eventplanner.screens.eventList.EventListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(fragment: EventAddAndEditFragment)
    fun inject(fragment: EventListFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}