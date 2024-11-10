package com.example.carhunter.ui.carForm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carhunter.data.model.Car
import com.example.carhunter.data.model.Place
import com.example.carhunter.data.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CarFormViewModel(private val repository: CarRepository = CarRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(CarFormUiState(car = null))
    val uiState: StateFlow<CarFormUiState> = _uiState

//    private val _carState = MutableStateFlow(Car())
//    val carState: StateFlow<Car> = _carState
////
//    private val _imageUri = MutableStateFlow<Uri?>(null)
//    val imageUri: StateFlow<Uri?> = _imageUri

    fun updateCar(name: String, year: String, license: String) {
        _uiState.value.car = _uiState.value.car?.copy(
            id = UUID.randomUUID().toString(),
            name = name,
            year = year,
            licence = license
        )
    }

//    fun setImageUri(uri: Uri) {
//        _imageUri.value = uri
//        print("URI = $uri")
//        _uiState.value.car = _uiState.value.car?.copy(imageUrl = uri)
//        //_carState.value = _carState.value.copy(imageUrl = uri.toString())
//    }

    fun saveCar() {
        println("save carr!")
        viewModelScope.launch {
            val car = Car(
                id = "1",
                name = "car name",
                licence = "abc-1234",
                imageUrl = "https://images.pexels.com/photos/210019/pexels-photo-210019.jpeg",
                year = "2020/2021",
                place = Place(
                    lat = -23.666,
                    long = -46.6333
                )
            )
            try {
                repository.saveCar(car)
            } catch (e: Exception) {
                println("Result ERROR $e")
                _uiState.value = _uiState.value.copy(hasError = true)
            }
        }
    }
}