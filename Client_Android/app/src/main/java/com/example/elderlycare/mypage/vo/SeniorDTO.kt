package com.example.elderlycare.mypage.vo

data class SeniorDTO (

    // user 정보
    val userId: Long,
    val roleStr: String,
    val email: String,
    val name: String,
    var address: String?,
//    var password: String,
    var phoneNumber: String?,
    var country: String?,
    var gender: String,
    var image: String?,

    // senior 정보
    val seniorId: Long,
    var serniorName: String?,
    var health: String?,
    var requirements: String?,
    var hasGuardian: Boolean?,

    // guardian 정보
    val guardianId: Long,
    var guardianName: String?,
    var guardianPhoneNumber: String?,
    var relationship: String?
)
