package com.example.elderlycare.board.service

import com.example.elderlycare.board.vo.BoardDTO
import com.example.elderlycare.board.vo.BoardVO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BoardService {
    @GET("boardlist")
    fun boardList() : Call<List<BoardVO>>

    @DELETE("delete/{num}")
    fun deleteBoard2(@Path("num") num: Long) : Call <Long>

    @POST("write")
    fun write(@Body boardDTO: BoardDTO) : Call<Void>

//    @GET("csrf-token")
//    fun getCsrfToken(): Call<Map<String, String>>
}