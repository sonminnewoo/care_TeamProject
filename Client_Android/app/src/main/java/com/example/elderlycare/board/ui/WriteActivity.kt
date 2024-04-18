package com.example.elderlycare.board.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.elderlycare.R
import com.example.elderlycare.board.service.BoardService
import com.example.elderlycare.board.vo.BoardDTO
import com.example.elderlycare.databinding.BoardWriteBinding
import com.example.elderlycare.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy

class WriteActivity : AppCompatActivity() {
    private lateinit var service: BoardService
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // EdgeToEdge 활성화
        enableEdgeToEdge()
        // 레이아웃 설정
        setContentView(R.layout.board_write)

        // View 바인딩
        val binding = BoardWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뒤로가기 버튼 클릭 이벤트 처리
        binding.iconBack.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Retrofit 설정 초기화
        setupRetrofit()
        // 글쓰기 버튼 클릭 이벤트 처리
        binding.btnWrite.setOnClickListener {
            // 제목과 내용 입력 값 가져오기
            val title = binding.title.text.toString()
            val content = binding.content.text.toString()

            // 새로운 BoardVO 객체 생성 (작성자, 작성일, 조회수, 댓글수는 서버에서 처리)
            val boardDTO = BoardDTO()
            boardDTO.title = title
            boardDTO.content = content
//            val boardVO = BoardVO(title, "", content, Date(), 0, 0, null)

            val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            val email = preferences.getString("user.email", "")
            val userId = preferences.getLong("user.userId", 0)
            boardDTO.writer = email!!
            boardDTO.userId = userId

            // Retrofit을 사용하여 서버에 POST 요청 보내기
            service.write(boardDTO).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("성공이다", "")
                        val intent = Intent(this@WriteActivity, ListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("실패", "응답 코드: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("실패", "요청 실패: ${t.message}")
                }
            })
        }

        val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userEmail = preferences.getString("user.email", "")
        Log.d(">>>>", "${userEmail}")

    }


    // Retrofit 설정 초기화 함수
    private fun setupRetrofit() {
        // Gson 객체 생성
        val gson: Gson = GsonBuilder().create()
        val client = setupOkHttpClient()

        // Retrofit Builder를 사용하여 Retrofit 객체 초기화
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL + "/m/board/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        // Retrofit을 사용하여 서비스 인터페이스 구현
        service = retrofit.create(BoardService::class.java)
    }
}

private fun setupOkHttpClient(): OkHttpClient {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        val cookieJar = JavaNetCookieJar(cookieManager)


        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
//            .addInterceptor(csrfInterceptor) // CSRF 토큰 인터셉터 추가
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
