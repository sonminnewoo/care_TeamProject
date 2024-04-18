package com.example.elderlycare.board.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.elderlycare.MainActivity
import com.example.elderlycare.R
import com.example.elderlycare.board.adapter.BoardAdapter
import com.example.elderlycare.board.adapter.OnBoardItemClickHandler
import com.example.elderlycare.board.service.BoardService
import com.example.elderlycare.board.vo.BoardVO
import com.example.elderlycare.databinding.BoardListBinding
import com.example.elderlycare.matching.view.FindCaregiversActivity
import com.example.elderlycare.matching.view.FindJobsActivity
import com.example.elderlycare.mypage.ui.CaregiverMypageActivity
import com.example.elderlycare.mypage.ui.SeniorMypageActivity
import com.example.elderlycare.ui.InfoActivity
import com.example.elderlycare.user.view.UserCheckActivity
import com.example.elderlycare.user.view.UserLoginActivity
import com.example.elderlycare.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListActivity : AppCompatActivity() {
    private lateinit var binding: BoardListBinding
    private lateinit var retrofit: Retrofit
    private lateinit var service: BoardService
    private lateinit var adapter: BoardAdapter

    private lateinit var navigationView: NavigationView
    private lateinit var navViewContainer: FrameLayout

    private var csrfToken: String? = null
    private var csrfHeaderName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_list)
        binding = BoardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //// 네비게이션

        navigationView = findViewById(R.id.nav_view)
        navViewContainer = findViewById(R.id.nav_view_container)

        // 헤더 레이아웃에서 버튼과 네비게이션 뷰 컨테이너 가져오기
        val headerLayout = findViewById<RelativeLayout>(R.id.header_layout)
        val btnMenu = headerLayout.findViewById<ImageButton>(R.id.btnMenu)
        navViewContainer = headerLayout.findViewById(R.id.nav_view_container)

        // 네비게이션 뷰 초기화
        navigationView = findViewById(R.id.nav_view)

        val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userRole = preferences.getString("user.role", "")

        // NavigationView 메뉴 아이템 선택 이벤트 처리
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.hc_info -> {
                    startActivity(Intent(this, InfoActivity::class.java))
                }
                R.id.nav_board -> {
                    startActivity(Intent(this, ListActivity::class.java))
                }
                R.id.nav_find_caregivers -> {
                    // nav_item3 선택 시 처리
                    startActivity(Intent(this, FindCaregiversActivity::class.java))
                }
                R.id.nav_find_jobs -> {
                    // nav_item3 선택 시 처리
                    startActivity(Intent(this, FindJobsActivity::class.java))
                }
                R.id.nav_myPage -> {
                    if(userRole == "SENIOR"){
                        startActivity(Intent(this, SeniorMypageActivity::class.java))
                    }
                    if(userRole == "CAREGIVER"){
                        startActivity(Intent(this, CaregiverMypageActivity::class.java))
                    }
                }
                R.id.nav_user_login-> {
                    // nav_item3 선택 시 처리
                    startActivity(Intent(this, UserLoginActivity::class.java))
                }R.id.nav_user_register-> {
                // nav_item3 선택 시 처리
                startActivity(Intent(this, UserCheckActivity::class.java))
            }
            }
            true// true 반환하여 클릭 이벤트 소비
        }

        // 메뉴 버튼 클릭 이벤트 처리
        btnMenu.setOnClickListener {
            toggleNavViewVisibility()
            // 네비게이션 뷰를 페이지 맨 위로 이동
            moveNavViewToTop()
        }
        // ImageView 클릭 이벤트 처리
        val imageViewLogo = findViewById<ImageView>(R.id.logo)
        imageViewLogo.setOnClickListener {
            // MainActivity로 돌아가는 Intent 생성
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }   //보완필요


        setupRetrofit()
//        fetchCsrfToken()
        adapter = BoardAdapter()


        getBoardList()

        //상세보기
        adapter.handler = object : OnBoardItemClickHandler {
            override fun onItemClick(holder: BoardAdapter.BoardViewHolder, v: View, idx: Int) {
                val vo = adapter.getBoard(idx)
                //액티비티 사이에 데이터를 주고 받을 땐 intent사용
                val intent = Intent(applicationContext, DetailActivity::class.java) //detail 액티비티
                intent.putExtra("board", vo) //데이터를 담아서 출력
//                Log.d(">>", "${holder.tvId.text}")
//                Log.d(">>", vo.toString())
//                arraylist 특징 (순서 o, 중복 o)
                startActivity(intent)
            }
        }

        binding.write.setOnClickListener {
            val intent = Intent(applicationContext, WriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toggleNavViewVisibility() {
        val navViewContainer = findViewById<FrameLayout>(R.id.nav_view_container)
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


    private fun setupRetrofit() {
//        val client = setupOkHttpClient()
        val gson: Gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm")
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL+"/m/board/")
            .addConverterFactory(GsonConverterFactory.create(gson))
//            .client(client)
            .build()

        service = retrofit.create(BoardService::class.java)


    }

    private fun getBoardList() {
        service.boardList().enqueue(object : Callback<List<BoardVO>> {
            override fun onResponse(call: Call<List<BoardVO>>, response: Response<List<BoardVO>>) {
                if (response.isSuccessful) {
                    val boardList: List<BoardVO> = response.body()!!
                    if (boardList != null) {
                        val arrayList: ArrayList<BoardVO> = ArrayList<BoardVO>(boardList)
                        adapter.setList(arrayList)
                        binding.boardList.adapter = adapter
                        binding.boardList.layoutManager =
                            LinearLayoutManager(this@ListActivity, RecyclerView.VERTICAL, false)

                    }
                    Log.d(">>>", "boardDTO: " + boardList)


                } else {
                    Log.e(">>", "Failed to fetch board info")
                }
            }

            override fun onFailure(call: Call<List<BoardVO>>, t: Throwable) {
                Log.e(">>", "Error: ${t.message}", t)
            }
        })
    }

//    private fun setupOkHttpClient(): OkHttpClient {
//        val cookieManager = CookieManager()
//        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
//        val cookieJar = JavaNetCookieJar(cookieManager)

    // CSRF 토큰을 요청 헤더에 추가하는 인터셉터
//        val csrfInterceptor = Interceptor { chain ->
//            csrfToken?.let { token ->
//                csrfHeaderName?.let { headerName ->
//                    // 요청을 수정하여 CSRF 토큰을 헤더에 추가
//                    val newRequest = chain.request().newBuilder()
//                        .addHeader(headerName, token)
//                        .build()
//                    chain.proceed(newRequest)
//                } ?: run {
//                    // CSRF 토큰 또는 헤더 이름이 null인 경우, 원래 요청을 그대로 진행
//                    chain.proceed(chain.request())
//                }
//            } ?: run {
//                // CSRF 토큰이 null인 경우, 원래 요청을 그대로 진행
//                chain.proceed(chain.request())
//            }
//        }

//        return OkHttpClient.Builder()
//            .cookieJar(cookieJar)
////            .addInterceptor(csrfInterceptor) // CSRF 토큰 인터셉터 추가
//            .addInterceptor(HttpLoggingInterceptor().apply {
//                level = HttpLoggingInterceptor.Level.BODY
//            })
//            .build()
//    }

//    private fun fetchCsrfToken() {
//        service.getCsrfToken().enqueue(object : Callback<Map<String, String>> {
//            override fun onResponse(
//                call: Call<Map<String, String>>,
//                response: Response<Map<String, String>>
//            ) {
//                if (response.isSuccessful) {
//                    csrfToken = response.body()?.get("token")
//                    csrfHeaderName = response.body()?.get("headerName")
//                    Log.d(">>>", "${csrfToken}")
//                } else {
//                    Log.e(">>", "Failed to fetch CSRF token")
//                }
//            }
//
//            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
//                Log.e(">>", "Error fetching CSRF token: ${t.message}", t)
//            }
//        })
//    }

}