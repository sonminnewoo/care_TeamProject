package com.example.elderlycare

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.elderlycare.adapter.SliderAdapter
import com.example.elderlycare.notice.adapter.NoticeAdapter
import com.example.elderlycare.notice.model.Notice
import com.example.elderlycare.notice.retrofit.RetrofitClient
import com.example.elderlycare.notice.view.NoticeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var sliderAdapter: SliderAdapter
    private val handler = Handler(Looper.getMainLooper())
    private val updateImageRunnable = Runnable {
        val currentItem = viewPager.currentItem
        val nextItem = if (currentItem == sliderAdapter.count - 1) 0 else currentItem + 1
        viewPager.setCurrentItem(nextItem, true)
    }
    private lateinit var noticeRecyclerView: RecyclerView
    private lateinit var noticeAdapter: NoticeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigationView()

        viewPager = findViewById(R.id.viewPager)
        val imageUrls = intArrayOf(
            R.drawable.care1,
            R.drawable.care2,
            R.drawable.care3
        )
        sliderAdapter = SliderAdapter(this, imageUrls)
        viewPager.adapter = sliderAdapter

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post(updateImageRunnable)
            }
        }, 3000, 3000)

        // RecyclerView 초기화(smallNotice : 메인페이지의  recyclerview >> 공지사항 리스트 최근순으로 최대 5개 출력기능을 아래에 fun에 추가)
        noticeRecyclerView = findViewById(R.id.smallNotice)
        noticeRecyclerView.layoutManager = LinearLayoutManager(this)

        // NoticeAdapter 생성 및 RecyclerView에 연결
        noticeAdapter = NoticeAdapter(this, mutableListOf())
        noticeRecyclerView.adapter = noticeAdapter

        // 공지사항 목록 로드
        loadSmallNotice()
    }

    fun openNoticeActivity(view: View) {
        val intent = Intent(this, NoticeActivity::class.java)
        startActivity(intent)
    }

    private fun loadSmallNotice() {
        val service = RetrofitClient.smallNoticeService
        val call = service.getSmallNotices(5)

        call.enqueue(object : Callback<List<Notice>>  {
            override fun onResponse(call: Call<List<Notice>> , response: Response<List<Notice>> ) {
                if (response.isSuccessful) {
                    val notices = response.body() ?: emptyList()
                    noticeAdapter.updateNoticeList(notices)
                } else {
                    // 실패 처리
                }
            }

            override fun onFailure(call: Call<List<Notice>> , t: Throwable) {
                // 오류 처리
            }
        })
    }
}