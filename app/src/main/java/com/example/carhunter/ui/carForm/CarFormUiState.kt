package com.example.carhunter.ui.carForm

import com.example.carhunter.data.model.Car

data class CarFormUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val hasCameraPermission: Boolean = false,
    val hasSuccessfullySaved: Boolean = false,
    var car: Car? = null,
)