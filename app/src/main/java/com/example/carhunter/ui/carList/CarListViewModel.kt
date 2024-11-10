package com.example.carhunter.ui.carList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carhunter.data.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarListViewModel(private val repository: CarRepository = CarRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(CarListUiState())
    val uiState: StateFlow<CarListUiState> = _uiState

    fun fetchCars() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(cars = repository.getCars())
            } catch (e: Exception) {
                println("Result ERROR $e")
                _uiState.value = _uiState.value.copy(hasError = true)
            }
        }
    }
}