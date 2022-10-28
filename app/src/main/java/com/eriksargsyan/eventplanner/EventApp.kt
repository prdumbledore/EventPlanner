package com.eriksargsyan.eventplanner

import android.app.Application
import android.content.Context
import com.eriksargsyan.eventplanner.di.AppComponent
import com.eriksargsyan.eventplanner.di.DaggerAppComponent

class EventApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()
    }

}

val Context.appComponent: AppComponent
    get() = when (this) {
        is EventApp -> appComponent
        else -> applicationContext.appComponent
    }