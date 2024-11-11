package com.example.carhunter.ui.carForm

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carhunter.Arguments
import com.example.carhunter.data.model.Car
import com.example.carhunter.data.model.Place
import com.example.carhunter.data.repository.CarRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CarFormViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val repository: CarRepository = CarRepository()
    private val _uiState = MutableStateFlow(CarFormUiState(car = null))
    val uiState: StateFlow<CarFormUiState> = _uiState

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private val carId: String = savedStateHandle
        .get<String>(Arguments.CAR_ID)
        ?.toString() ?: ""

    init {
        if (carId.isNotEmpty()) {
            println("carId = $carId")
            loadCar()
        }
    }

    private fun loadCar() {
        _uiState.value = _uiState.value.copy(hasError = false, isLoading = true)

        viewModelScope.launch {
            try {
                val carResponse = repository.getCar(carId)
                _uiState.value = _uiState.value.copy(
                    car = carResponse,
                    isLoading = false
                )
                // _imageUri.value = Uri.parse(carResponse.imageUrl) to-do: download image on device..
            } catch (e: Exception) {
                println("Error on get car by id: $e")
                _uiState.value = _uiState.value.copy(hasError = true, isLoading = false)
            }
        }
    }


    private fun uploadImageToFirebase(imageUri: Uri, isNew: Boolean) {
        println("imageUri firebase = $imageUri")
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = "images/${UUID.randomUUID()}.jpg"
        val imagesRef = storageRef.child(fileName)

        imagesRef.putFile(imageUri)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    if (isNew) {
                        saveCar(imageUrl = downloadUri.toString())
                    } else {
                        updateCar(imageUrl = downloadUri.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                _uiState.value = _uiState.value.copy(hasError = true)
                println("Error on upload image to firebase: $exception")
            }
    }

    fun setImageUri(uri: Uri) {
        _imageUri.value = uri
    }

    fun updateCameraPermissionStatus(isGranted: Boolean) {
        _uiState.value = _uiState.value.copy(hasCameraPermission = isGranted)
    }

    private fun validateForm(): Boolean {
        if (_imageUri.value == null) {
            println("sem imagem para upload!")
            return false
        }

        if (_uiState.value.car?.name?.isEmpty() == true ||
            _uiState.value.car?.year?.isEmpty() == true ||
            _uiState.value.car?.licence?.isEmpty() == true
        ) {
            println("preencha todos os campos! ${_uiState.value.car}")
            return false
        }

        // to-do validations

        return true
    }

    fun validateAndSaveCard(name: String, year: String, licence: String) {
        if (!validateForm()) {
            println("dados inválidos!")
            _uiState.value = _uiState.value.copy(hasError = true)
            return
        }

        val isNew = carId.isEmpty()

        val car = Car(
            id = if (isNew) UUID.randomUUID().toString() else carId,
            name = name,
            year = year,
            licence = licence,
            place = Place(lat = -10.0, long = -20.0), // to-do
            imageUrl = ""
        )

        _uiState.value.car = car
        uploadImageToFirebase(_imageUri.value!!, isNew)
    }

    private fun saveCar(imageUrl: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val car = _uiState.value.car?.copy(imageUrl = imageUrl)
            try {
                repository.saveCar(car!!)
                _uiState.value = _uiState.value.copy(isLoading = false, hasSuccessfullySaved = true)
            } catch (e: Exception) {
                println("Error on save car: $e")
                _uiState.value = _uiState.value.copy(hasError = true)
            }
        }
    }

    private fun updateCar(imageUrl: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val car = _uiState.value.car?.copy(imageUrl = imageUrl)
            try {
                if (car != null) {
                    repository.updateCar(carId = car.id, car = car)
                }
                _uiState.value = _uiState.value.copy(isLoading = false, hasSuccessfullySaved = true)
            } catch (e: Exception) {
                println("Error on update car: $e")
                _uiState.value = _uiState.value.copy(hasError = true)
            }
        }
    }
}