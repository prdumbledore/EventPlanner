package com.eriksargsyan.eventplanner.di

import dagger.Module

@Module(includes = [RetrofitModule::class, RepositoryModule::class])
class AppModule