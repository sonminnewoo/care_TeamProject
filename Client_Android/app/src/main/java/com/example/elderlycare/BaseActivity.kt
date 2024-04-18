package com.example.elderlycare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.elderlycare.board.ui.ListActivity
import com.example.elderlycare.matching.view.FindCaregiversActivity
import com.example.elderlycare.matching.view.FindJobsActivity
import com.example.elderlycare.mypage.ui.CaregiverMypageActivity
import com.example.elderlycare.mypage.ui.SeniorMypageActivity
import com.example.elderlycare.ui.InfoActivity
import com.example.elderlycare.user.view.UserCheckActivity
import com.example.elderlycare.user.view.UserLoginActivity
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity() {
    lateinit var navigationView: NavigationView
    lateinit var navViewContainer: FrameLayout
    private lateinit var btnLogin: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        updateLoginLogoutButtons()
    }

    private fun updateLoginLogoutButtons() {
        val headerLayout = findViewById<RelativeLayout>(R.id.header_layout)
        btnLogin = headerLayout.findViewById(R.id.btnLogin)
        btnLogout = headerLayout.findViewById(R.id.btnLogout)

        val isLoggedIn = isUserLoggedIn()

        if (isLoggedIn) {
            btnLogin.visibility = View.GONE
            btnLogout.visibility = View.VISIBLE
        } else {
            btnLogin.visibility = View.VISIBLE
            btnLogout.visibility = View.GONE
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val email = preferences.getString("user.email", null)
        return email != null
    }

    fun setupNavigationView() {
        navigationView = findViewById(R.id.nav_view)
        navViewContainer = findViewById(R.id.nav_view_container)

        val headerLayout = findViewById<RelativeLayout>(R.id.header_layout)
        val btnMenu = headerLayout.findViewById<ImageButton>(R.id.btnMenu)
        navViewContainer = headerLayout.findViewById(R.id.nav_view_container)

        navigationView = findViewById(R.id.nav_view)
        btnLogin = headerLayout.findViewById(R.id.btnLogin)
        btnLogout = headerLayout.findViewById(R.id.btnLogout)

        val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userRole = preferences.getString("user.role", "")

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
            true
        }

        btnMenu.setOnClickListener {
            toggleNavViewVisibility()
            moveNavViewToTop()
        }

        val imageViewLogo = findViewById<ImageView>(R.id.logo)
        imageViewLogo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //로그인 버튼 클릭 이벤트 처리
        btnLogin.setOnClickListener {
            val intent = Intent(this, UserLoginActivity::class.java)
            startActivity(intent)
        }

        //로그아웃 버튼 클릭 이벤트 처리
        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.remove("user.email")
        editor.remove("user.role")
        editor.apply()

        updateLoginLogoutButtons()

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun toggleNavViewVisibility() {
        if (navViewContainer.visibility == View.VISIBLE) {
            navViewContainer.visibility = View.GONE
        } else {
            navViewContainer.visibility = View.VISIBLE
        }
    }

    private fun moveNavViewToTop() {
        ViewCompat.offsetTopAndBottom(navViewContainer, -navViewContainer.top)
    }
}