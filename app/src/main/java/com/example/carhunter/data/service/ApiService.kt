package com.example.carhunter.data.service

import com.example.carhunter.data.model.Car
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("car")
    suspend fun getCars(): List<Car>

    @POST("car")
    suspend fun saveCar(@Body item: Car): Car

    @GET("car/{id}")
    suspend fun getCar(@Path("id") id: String): Car

    @PATCH("car/{id}")
    suspend fun updateCar(@Path("id") id: String, @Body car: Car): Car
}