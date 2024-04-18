package com.example.elderlycare.matching.model

data class Caregiver(
    val name: String,
    val country: String,
    val experience: String,
    val experienceYears: Int,
    val certification: String,
    val availableHours: String,
    val image: String,
    val gender: String,
    val caregiverId: Int,
)