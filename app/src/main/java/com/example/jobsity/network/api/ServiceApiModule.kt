package com.example.jobsity.network.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


//Hilt Object Module creation for API
@InstallIn(SingletonComponent::class)
@Module
object ServiceApiModule {


    //Base URL for API
    @Provides
    fun provideBaseUrl() : String = "https://api.tvmaze.com"

    //Build Moshi JSON
    @Provides
    @Singleton
    fun provideMoshi(): Moshi =  Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //Build retrofit to get API data
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, BASE_URL:String): Retrofit =
        Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    @Provides
    @Singleton
    fun provideServiceApi(retrofit: Retrofit): ServiceApi = retrofit.create(ServiceApi::class.java)

    @Provides
    @Singleton
    fun provideServiceHelper(serviceApiHelper: ServiceApiHelperImpl): ServiceApiHelper = serviceApiHelper


}