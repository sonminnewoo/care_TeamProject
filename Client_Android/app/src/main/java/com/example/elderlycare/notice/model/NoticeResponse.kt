package com.example.elderlycare.notice.model

import com.google.gson.annotations.SerializedName

data class NoticeResponse(
    val content: List<Notice>,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val first: Boolean,
    val size: Int,
    val number: Int,
    val numberOfElements: Int,
    val empty: Boolean
)

data class Notice(
    val num: Long,
    val title: String,
    val writer: String,
    val content: String,
    @SerializedName("regdate") val regDate: String,
    val hitcount: Long,
    val replycnt: Long,
    val user: User
)

data class User(
    @SerializedName("userId") val userId: Long,
    val role: String,
    val name: String,
    val email: String,
    val address: String,
    val password: String,
    val phoneNumber: String?,
    val country: String?,
    val gender: String?,
    val image: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
)