package com.eriksargsyan.eventplanner.di


import dagger.Module


@Module(includes = [
    RetrofitModule::class,
    RepositoryModule::class,
    RoomModule::class,
    PicassoModule::class
])
class AppModule