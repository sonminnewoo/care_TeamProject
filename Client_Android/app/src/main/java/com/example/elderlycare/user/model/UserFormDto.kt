package com.example.elderlycare.model

data class UserFormDto(
    val name: String,
    val email: String,
    val address: String,
    val password: String,
    val confirmPassword: String,
    val country: String,
    val gender: String,
    val phoneNumber: String,
    val image: String?,
    val seniorName: String?,
    val health: String?,
    val requirements: String?,
    val hasGuardian: Boolean?,
    val guardianName: String?,
    val guardianPhoneNumber: String?,
    val relationship: String?,
    val certification: String?,
    val specialization: String?,
    val experience: String?,
    val experienceYears: Int?,
    val availableHours: String?
)