package com.example.elderlycare.matching.model

data class MatchingRequestDto(
    var userId: Long? = null,
    var caregiverId: Long? = null,
    var matchingCountry: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var status: String? = null,
    var userRole: String? = null,
    var health: String? = null,
    var requirements: String? = null,
    var hasGuardian: Boolean? = null,
    var guardianName: String? = null,
    var relationship: String? = null,
    var elderlyName: String? = null,
    var elderlyGender: String? = null
)