package com.example.elderlycare.mypage.vo

data class MatchingDTO (
    var id: Long,
    var seniorId: Long,
    var caregiverId : Long,
    var matchingCountry: String,
    var startDate: String? = null,
    var endDate: String? = null,
    var startTime: String? = null,
    var endTime: String? = null,
    var status: String? = null,

//    var startDateStr: String,
//    var endDateStr: String,
//    var startTimeStr: String,
//    var endTimeStr: String,

    var matchingUserName: String

)