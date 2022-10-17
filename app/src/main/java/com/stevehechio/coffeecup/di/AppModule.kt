package com.stevehechio.coffeecup.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stevehechio.coffeecup.data.local.db.AppDatabase
import com.stevehechio.coffeecup.data.remote.api.CoffeeApiService
import com.stevehechio.coffeecup.data.repository.CoffeeRepository
import com.stevehechio.coffeecup.ui.activities.home.CoffeeViewModel
import com.stevehechio.coffeecup.utils.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
// Created by Steve Hechio on 17/10/2022.
 */

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideCache(application: Application): Cache {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(application.cacheDir, "http-cache")
        return Cache(httpCacheDirectory, cacheSize)
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.cache(cache)
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(httpClient)
            .baseUrl(AppConstants.BASE_URL)
            .build()
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.invoke(context)
    }

    @Provides
    @Singleton
    fun provideCoffeeService(retrofit: Retrofit): CoffeeApiService =
        retrofit.create(CoffeeApiService::class.java)

    @Provides
    @Singleton
    fun provideCoffeeRepository(@ApplicationContext context: Context,
                                service: CoffeeApiService,
                                appDatabase: AppDatabase):
            CoffeeRepository = CoffeeRepository(context, service, appDatabase)

    @Provides
    @Singleton
    fun provideCoffeeViewModel(repository: CoffeeRepository): CoffeeViewModel =
        CoffeeViewModel(repository = repository)
}