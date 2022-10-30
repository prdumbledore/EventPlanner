package com.eriksargsyan.eventplanner.di

import android.content.Context
import com.eriksargsyan.eventplanner.screens.eventAddAndEdit.EventAddAndEditFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(fragment: EventAddAndEditFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}