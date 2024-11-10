package com.example.carhunter.ui.carList

import com.example.carhunter.data.model.Car

data class CarListUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val cars: List<Car> = emptyList()
)