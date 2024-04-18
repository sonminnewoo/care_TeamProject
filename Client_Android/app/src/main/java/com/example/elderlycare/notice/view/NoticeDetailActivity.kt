package com.example.elderlycare.notice.view

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.elderlycare.R
import com.example.elderlycare.notice.model.Notice
import com.example.elderlycare.notice.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeDetailActivity : AppCompatActivity() {
    private lateinit var titleTextView: TextView
    private lateinit var writerTextView: TextView
    private lateinit var contentTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_notice_layout)

        titleTextView = findViewById(R.id.recyclerTitle_view)
        writerTextView = findViewById(R.id.recyclerWriter_view)
        contentTextView = findViewById(R.id.recyclerContent_view)

        // Intent에서 전달된 공지사항 ID를 가져옵니다.
        val noticeNum = intent.getLongExtra("notice_num", -1)

        // 가져온 ID를 사용하여 해당 공지사항의 상세 정보를 표시합니다.
        loadDetailNotice(noticeNum)
        // 상세 정보를 표시하는 코드 작성
    }
    private fun loadDetailNotice(noticeId: Long) {
        val service = RetrofitClient.noticeDetailService
        val call = service.getNoticeDetail(noticeId)

        call.enqueue(object : Callback<Notice> {
            override fun onResponse(call: Call<Notice>, response: Response<Notice>) {
                if (response.isSuccessful) {
                    val notice = response.body()
                    if (notice != null) {
                        // 공지사항의 상세 정보를 표시합니다.
                        displayDetailNotice(notice)
                    } else {
                        Log.e("NoticeDetail", "Notice data is null.")
                    }
                } else {
                    Log.e("NoticeDetail", "Failed to get notice detail: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<Notice>, t: Throwable) {
                Log.e("NoticeDetail", "Error getting notice detail", t)
            }
        })
    }

    private fun displayDetailNotice(notice: Notice) {
        titleTextView.text = notice.title
        writerTextView.text = notice.writer
        contentTextView.text = notice.content
    }
}