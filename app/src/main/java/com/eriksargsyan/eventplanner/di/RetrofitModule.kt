package com.eriksargsyan.eventplanner.di

import com.eriksargsyan.eventplanner.data.network.EventAPI
import com.eriksargsyan.eventplanner.util.Constants.BASE_URL
import com.eriksargsyan.eventplanner.util.Dispatchers
import com.eriksargsyan.eventplanner.util.IO
import com.eriksargsyan.eventplanner.util.NetworkCityNameMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module

import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
object RetrofitModule {

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()

    @Provides
    @Singleton
    fun provideEventAPI(retrofit: Retrofit): EventAPI =
        retrofit.create(EventAPI::class.java)

    @Provides
    @Singleton
    fun provideNetworkMapper(): NetworkCityNameMapper = NetworkCityNameMapper()

    @Provides
    @IO
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

}