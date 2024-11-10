package com.example.carhunter.data.repository

import com.example.carhunter.data.model.Car
import com.example.carhunter.data.service.RetrofitClient.apiService

class CarRepository {
        suspend fun getCars(): List<Car> {
            return apiService.getCars()
        }
}