package com.example.elderlycare.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.elderlycare.MainActivity
import com.example.elderlycare.R
import com.example.elderlycare.board.ui.ListActivity
import com.example.elderlycare.matching.view.FindCaregiversActivity
import com.example.elderlycare.matching.view.FindJobsActivity
import com.example.elderlycare.mypage.ui.CaregiverMypageActivity
import com.example.elderlycare.mypage.ui.SeniorMypageActivity
import com.example.elderlycare.user.view.UserCheckActivity
import com.example.elderlycare.user.view.UserLoginActivity
import com.google.android.material.navigation.NavigationView

class InfoActivity : AppCompatActivity() {

    private lateinit var navigationView: NavigationView
    private lateinit var navViewContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_layout)


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

}