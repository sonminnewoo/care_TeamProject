package com.example.elderlycare.matching.model

data class MatchingDetail(
    val caregiverUser: User,
    val caregiver: Caregiver,
    val seniorUser: User,
    val senior: Senior,
    val matching: Matching
)



