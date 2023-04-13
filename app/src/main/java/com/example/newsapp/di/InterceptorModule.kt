package com.example.newsapp.di

import com.example.newsapp.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Provides
    @ApiKeyInterceptor
    @Singleton
    fun provideApiKeyInterceptor() = Interceptor { chain ->
        val request = chain.request()

        val newRequest = request.newBuilder()
            .addHeader("X-Api-Key", BuildConfig.API_KEY)
            .build()

        chain.proceed(newRequest)
    }

    @Provides
    @LoggingInterceptor
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
}