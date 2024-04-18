package com.example.elderlycare.mypage.vo

data class MatchingResponse (
    val pastMatchings: List<MatchingDTO>,
    val progressMatchings: List<MatchingDTO>,

)