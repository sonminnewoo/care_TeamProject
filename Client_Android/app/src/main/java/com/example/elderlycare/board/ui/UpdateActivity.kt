package com.example.elderlycare.board.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.elderlycare.R
import com.example.elderlycare.databinding.BoardWriteBinding
import com.google.android.material.navigation.NavigationView

class UpdateActivity : AppCompatActivity() {
    private lateinit var navigationView: NavigationView
    private lateinit var navViewContainer: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.board_write)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_write)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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


        var binding = BoardWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)








    }



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