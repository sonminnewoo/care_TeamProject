package com.example.elderlycare.matching.model

data class User(
    val userId: Int,
    val role: String,
    val name: String,
    val email: String,
    val address: String,
    val password: String,
    val phoneNumber: String,
    val country: String,
    val gender: String,
    val image: String,
    val createdAt: String,
    val updatedAt: String
)