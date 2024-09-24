package com.example.androidtaskmayank.dataLayer.modules

import com.example.androidtaskmayank.dataLayer.CalendarTaskApiService
import com.example.androidtaskmayank.repositories.TasksRepo
import com.example.androidtaskmayank.repositories.TasksRepoImpl
import com.example.androidtaskmayank.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BODY) })
        .build()


    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): CalendarTaskApiService =
        retrofit.create(CalendarTaskApiService::class.java)


    @Singleton
    @Provides
    fun provideCalendarTaskApiService(apiService: CalendarTaskApiService): TasksRepo =
        TasksRepoImpl(apiService)

}
