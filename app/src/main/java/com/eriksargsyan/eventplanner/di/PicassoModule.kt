package com.eriksargsyan.eventplanner.di

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module
object PicassoModule {

    @Provides
    @Singleton
    fun providePicasso(
        context: Context,
        okHttpDownloader: OkHttp3Downloader
    ): Picasso? {
        return Picasso.Builder(context)
            .downloader(okHttpDownloader)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpDownloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }
}