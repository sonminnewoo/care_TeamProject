package com.example.elderlycare.matching.model

data class Matching(
    val id: Int,
    val seniorId: Int,
    val caregiverId: Int,
    val matchingCountry: String,
    val startDate: String,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val status: String,
)