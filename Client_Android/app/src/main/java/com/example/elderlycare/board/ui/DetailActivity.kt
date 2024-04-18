package com.example.elderlycare.board.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.elderlycare.R
import com.example.elderlycare.board.service.BoardService
import com.example.elderlycare.board.vo.BoardVO
import com.example.elderlycare.databinding.BoardDetailBinding
import com.example.elderlycare.utils.Constants
import com.example.elderlycare.utils.getParcelable
import com.example.elderlycare.utils.showNoti
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: BoardDetailBinding
    private lateinit var service: BoardService
    private lateinit var navigationView: NavigationView
    private lateinit var navViewContainer: FrameLayout
    private lateinit var retrofit: Retrofit
    private var isNavViewVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.board_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.board_detail)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        binding = BoardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //// 네비게이션
        navigationView = findViewById(R.id.board_detail_nav_view)
        navViewContainer = findViewById(R.id.board_detail_nav_view_container)

        // 헤더 레이아웃에서 버튼과 네비게이션 뷰 컨테이너 가져오기
        val headerLayout = findViewById<RelativeLayout>(R.id.board_detail_header_layout)
        val btnMenu = headerLayout.findViewById<ImageButton>(R.id.btnMenu)
        navViewContainer = headerLayout.findViewById(R.id.board_detail_nav_view_container)

        // 네비게이션 뷰 초기화
        navigationView = findViewById(R.id.board_detail_nav_view)



//        // NavigationView 메뉴 아이템 선택 이벤트 처리
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_edit -> {
                    // nav_item1 선택 시 처리
                    startActivity(Intent(this, WriteActivity::class.java))
                }
                R.id.nav_delete -> {
                    // nav_item2 선택 시 처리
                    startActivity(Intent(this, ListActivity::class.java))
                }

                // 다른 메뉴 아이템에 대한 처리
            }
            true// true 반환하여 클릭 이벤트 소비
        }

//        // 메뉴 버튼 클릭 이벤트 처리
        btnMenu.setOnClickListener {
            toggleNavViewVisibility()
            // 네비게이션 뷰를 페이지 맨 위로 이동
            moveNavViewToTop()
        }

//        val boardDetail :BoardDetailHeaderLayoutBinding = BoardDetailHeaderLayoutBinding.inflate(layoutInflater)
        setupRetrofit()

        var vo: BoardVO? =null

        if (intent.hasExtra("board")) {
            vo = intent.getParcelable("board", BoardVO::class.java)
        }
        if(vo == null){
            showNoti(this, "Null", "없는 데이터입니다.")
            finish()
            return
        }

        binding.tvTitle.text = vo.title
        binding.tvWriter.text = vo.writer
        binding.tvDate.text = vo.regdate.toString()
        binding.tvContents.text = vo.content
        binding.tvHitCnt.text = vo.hitcount.toString()
        binding.tvCmtCnt.text = vo.replycnt.toString()


        val btnDelete = binding.btnDelete
        btnDelete.setOnClickListener {
            var boardNum = vo.num
            Log.d("표시", "${boardNum}")
            deleteBoard(boardNum)
            finish()
            var intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        val btnUpdate = binding.btnUpdate
        btnUpdate.setOnClickListener {
            var boardNum = vo.num
            var intent = Intent(this, WriteActivity::class.java)
            startActivity(intent)
            finish()

        }

    }

    private fun setupRetrofit() {
//        val client = setupOkHttpClient()
        val gson: Gson = GsonBuilder()
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL+"/m/board/")
            .addConverterFactory(GsonConverterFactory.create(gson))
//            .client(client)
            .build()

        service = retrofit.create(BoardService::class.java)
    }

    private fun deleteBoard(num: Long) {
        service.deleteBoard2(num).enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    Log.d(">>>>", "board Num: ${response})")
                }
                else {
                    Log.e(">>", "Failed to delete board")
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                Log.e(">>", "Error: ${t.message}", t)
            }
        })
    }

//    private fun setupOkHttpClient(): OkHttpClient {
//        val cookieManager = CookieManager()
//        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
//        val cookieJar = JavaNetCookieJar(cookieManager)
//


//        return OkHttpClient.Builder()
////            .cookieJar(cookieJar)
////            .addInterceptor(csrfInterceptor) // CSRF 토큰 인터셉터 추가
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//            .build()
//    }




    private fun toggleNavViewVisibility() {
        val navViewContainer = findViewById<FrameLayout>(R.id.board_detail_nav_view_container)
        if (navViewContainer.visibility == View.VISIBLE) {
            // 네비게이션 뷰가 보이는 경우, 유지
            navViewContainer.visibility = View.GONE
        } else {
            // 네비게이션 뷰가 숨겨진 경우, 보이게 함
            navViewContainer.visibility = View.VISIBLE
        }
    }
    private fun moveNavViewToTop() {
        // 네비게이션 뷰를 페이지 맨 위로 이동
        ViewCompat.offsetTopAndBottom(navViewContainer, -navViewContainer.top)
    }
}