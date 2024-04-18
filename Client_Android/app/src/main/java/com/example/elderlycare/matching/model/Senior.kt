package com.example.elderlycare.matching.model

data class Senior(
    val seniorId: Int,
    val seniorName: String,
    val health: String,
    val requirements: String,
    val hasGuardian: Boolean
)