package com.example.carhunter.data.model

data class Car(
    val id: String,
    val name: String,
    val year: String,
    val imageUrl: String,
    val licence: String,
    val place: Place
)