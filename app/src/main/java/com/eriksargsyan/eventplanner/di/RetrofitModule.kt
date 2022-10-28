package com.eriksargsyan.eventplanner.di

import com.eriksargsyan.eventplanner.data.EventAPI
import com.eriksargsyan.eventplanner.util.Constants.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module

import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
object RetrofitModule {

    @Provides
    fun provideGson(): Gson =
        GsonBuilder().create()

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    @Provides
    fun provideRetrofit(gson: Gson, httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()

    @Provides
    fun provideEventAPI(retrofit: Retrofit): EventAPI =
        retrofit.create(EventAPI::class.java)

}