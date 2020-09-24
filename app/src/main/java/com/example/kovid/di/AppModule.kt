package com.example.kovid.di

import android.content.Context
import com.example.kovid.data.local.AppDatabase
import com.example.kovid.data.local.CovidDao
import com.example.kovid.data.remote.CovidRemoteDataSource
import com.example.kovid.data.remote.CovidService
import com.example.kovid.data.repository.CovidRepository
import com.example.kovid.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun providesGson() : Gson = GsonBuilder().create()

    @Provides
    fun provideCovidService(retrofit: Retrofit) : CovidService = retrofit.create(CovidService::class.java)

    @Singleton
    @Provides
    fun provideCovidRemoteDataSource(covidService: CovidService) = CovidRemoteDataSource(covidService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext : Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCovidDao(db: AppDatabase) = db.covidDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: CovidRemoteDataSource, localDataSource: CovidDao) = CovidRepository(remoteDataSource,localDataSource)
}