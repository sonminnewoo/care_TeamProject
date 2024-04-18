package com.example.elderlycare.notice.view

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elderlycare.BaseActivity
import com.example.elderlycare.R
import com.example.elderlycare.notice.adapter.NoticeAdapter
import com.example.elderlycare.notice.model.Notice
import com.example.elderlycare.notice.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)    //activty_notice >> 공지사항 리스트

        setupNavigationView() // 네비게이션 뷰 설정

        //listNotice(메인페이지 공지사항 텍스트 누르고 이동 >> 공지사항 리스트의 recyclerview)
        recyclerView = findViewById(R.id.listNotice)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // NoticeAdapter 초기화 및 연결
        noticeAdapter = NoticeAdapter(this, mutableListOf())
        recyclerView.adapter = noticeAdapter

        // 공지사항 데이터 로드
        loadNotice()
    }

    private fun loadNotice() {
        // 공지사항 데이터 가져오기
        val noticeService = RetrofitClient.noticeService
        noticeService.getAllNotices().enqueue(object : Callback<List<Notice>> { // Callback<NoticeResponse>로 수정
                override fun onResponse(call: Call<List<Notice>>, response: Response<List<Notice>>) { // Response<NoticeResponse>로 수정
                    if (response.isSuccessful) {
                        val notices = response.body() ?: emptyList() // NoticeResponse에서 공지 목록 가져오기
                        noticeAdapter.updateNoticeList(notices)
                    } else {
                        Log.e("Notice", "Failed to get notices: ${response.errorBody()}")
                    }
                }

                override fun onFailure(
                    call: Call<List<Notice>>,
                    t: Throwable
                ) { // Callback<NoticeResponse>로 수정
                    Log.e("Notice", "Error getting notices", t)
                }
            })
    }
}
