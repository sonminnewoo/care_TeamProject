package com.example.elderlycare.notice.retrofit

import com.example.elderlycare.notice.model.Notice
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NoticeService {
    @GET("/m/notices/main-notices")
    fun getSmallNotices(@Query("limit") limit: Int) : Call<List<Notice>>
    @GET("/m/notices")
    fun getAllNotices(): Call<List<Notice>>

    @GET("/m/notices/{id}")
    fun getNoticeDetail(@Path("id") noticeId: Long): Call<Notice>
}