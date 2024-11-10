package com.example.carhunter.data.model

data class Car(
    val id: String,
    val name: String,
    val year: String,
    val imageUrl: String,
    val license: String,
    val place: Place
)