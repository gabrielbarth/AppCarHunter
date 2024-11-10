package com.example.carhunter.ui.carForm

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carhunter.data.model.Car
import com.example.carhunter.data.model.Place
import com.example.carhunter.data.repository.CarRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CarFormViewModel(private val repository: CarRepository = CarRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(CarFormUiState(car = null))
    val uiState: StateFlow<CarFormUiState> = _uiState

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri

    private fun uploadImageToFirebase(imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = "images/${UUID.randomUUID()}.jpg"
        val imagesRef = storageRef.child(fileName)

        imagesRef.putFile(imageUri)
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveCar(imageUrl = downloadUri.toString())
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

        if (_imageUri.value.toString().isEmpty()) {
            println("sem URI!")
            return false
        }

        // to-do validations

        return true
    }

    fun validateAndSaveCard(name: String, year: String, license: String) {

        if (!validateForm()) {
            _uiState.value = _uiState.value.copy(hasError = true)
        }

        val car = Car(
            id = UUID.randomUUID().toString(),
            name = name,
            year = year,
            licence = license,
            place = Place(lat = -10.0, long = -20.0), // to-do
            imageUrl = ""
        )

        _uiState.value.car = car
        uploadImageToFirebase(_imageUri.value!!)
    }

    private fun saveCar(imageUrl: String) {
        viewModelScope.launch {
            val car = _uiState.value.car?.copy(imageUrl = imageUrl)
            try {
                repository.saveCar(car!!)
            } catch (e: Exception) {
                println("Error on save car: $e")
                _uiState.value = _uiState.value.copy(hasError = true)
            }
        }
    }
}