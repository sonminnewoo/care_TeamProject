package com.example.elderlycare.service

import com.example.elderlycare.model.UserFormDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

public interface UserService {
    @POST("/m/user/register")
    fun registerUser(@Body userFormDto: UserFormDto);
}
