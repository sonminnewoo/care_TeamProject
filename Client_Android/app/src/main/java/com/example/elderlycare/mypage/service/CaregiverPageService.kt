package com.example.elderlycare.mypage.service

import com.example.elderlycare.mypage.vo.CaregiverDTO
import com.example.elderlycare.mypage.vo.MatchingDTO
import com.example.elderlycare.mypage.vo.MatchingResponse
import com.example.elderlycare.mypage.vo.SeniorDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CaregiverPageService {

    @GET("caregiverInfo/{userId}")
    fun caregiverInfo(@Path("userId") userId: Long): Call<CaregiverDTO>

    @GET("matchingInfo/{userId}")
    fun matchingInfo(@Path("userId") userId: Long): Call<MatchingResponse>

}