package com.example.carhunter.data.repository

import com.example.carhunter.data.model.Car
import com.example.carhunter.data.service.RetrofitClient.apiService

class CarRepository {
    suspend fun getCars(): List<Car> {
        return apiService.getCars()
    }

    suspend fun saveCar(car: Car): Car {
        return apiService.saveCar(car)
    }

    suspend fun getCar(carId: String): Car {
        return apiService.getCar(carId)
    }

    suspend fun updateCar(carId: String, car: Car): Car {
        return apiService.updateCar(id = carId, car = car)
    }
}