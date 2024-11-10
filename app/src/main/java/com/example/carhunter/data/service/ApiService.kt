package com.example.carhunter.data.service

import com.example.carhunter.data.model.Car
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("car")
    suspend fun getCars(): List<Car>

    @POST("car")
    suspend fun saveCar(@Body item: Car): Car
}