package com.eriksargsyan.eventplanner.util

import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton


@Singleton
object Dispatchers {

    @IO
    val IO = Dispatchers.IO

    @Main
    val Main = Dispatchers.Main

    @Default
    val Default = Dispatchers.Default


}

@Qualifier
annotation class IO

@Qualifier
annotation class Default

@Qualifier
annotation class Main