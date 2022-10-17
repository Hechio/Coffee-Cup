package com.stevehechio.coffeecup.data.remote.api

import com.stevehechio.coffeecup.data.remote.model.CoffeeApiResponse
import com.stevehechio.coffeecup.data.remote.model.CoffeeRandomResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
// Created by Steve Hechio on 14/10/2022.
 */
interface CoffeeApiService {

    @GET("{type}")
    fun getCoffeeDetails(@Path("type") type: String): Observable<CoffeeApiResponse>
}

interface RandomCoffeeService{
    @GET("random.json/")
    fun getRandomCoffee(): Observable<CoffeeRandomResponse>
}