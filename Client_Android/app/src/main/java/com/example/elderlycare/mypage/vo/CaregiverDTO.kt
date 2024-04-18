package com.example.elderlycare.mypage.vo

data class CaregiverDTO(

    // user 정보
    val userId: Long,
    val roleStr: String,
    val email: String,
    val name: String,
    var address: String?,
    var phoneNumber: String?,
    var country: String?,
    var gender: String,
    var image: String?,

    // caregiver 정보
    val caregiverId: Long,
    val caregiverName: String?,
    var experience: String?,
    var certification: String?,
    var availableHours: String?,
    var specialization: String?,
    var experienceYears: Int?


)
