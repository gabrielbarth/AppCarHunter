package com.example.carhunter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "car_location_table")
data class Place(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var lat: Double = 0.toDouble(),
    var long: Double = 0.toDouble(),
    var createdAt: Date = Date()
)