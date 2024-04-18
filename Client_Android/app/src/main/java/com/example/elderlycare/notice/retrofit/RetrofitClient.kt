package com.example.elderlycare.notice.retrofit

import com.example.elderlycare.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = Constants.BASE_URL // 서버의 기본 URL로 변경해야 합니다. + ip : 포트번호

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val smallNoticeService: NoticeService by lazy {
        retrofit.create(NoticeService::class.java)
    }

    val noticeService: NoticeService by lazy {
        retrofit.create(NoticeService::class.java)
    }

    val noticeDetailService: NoticeService by lazy {
        retrofit.create(NoticeService::class.java)
    }
}