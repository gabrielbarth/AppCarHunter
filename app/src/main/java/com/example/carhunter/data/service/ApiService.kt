package com.example.carhunter.data.service

import com.example.carhunter.data.model.Car
import retrofit2.http.GET

interface ApiService {
    @GET("car")
    suspend fun getCars(): List<Car>
}