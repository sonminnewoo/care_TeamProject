package com.example.elderlycare.user.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.elderlycare.R
import com.example.elderlycare.adapter.SliderAdapter

class UserCheckActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var sliderAdapter: SliderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_check)

        val cbTermsOfService = findViewById<CheckBox>(R.id.cbTermsOfService)
        val cbPrivacyPolicy = findViewById<CheckBox>(R.id.cbPrivacyPolicy)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            if (!cbTermsOfService.isChecked || !cbPrivacyPolicy.isChecked) {
                Toast.makeText(this, "[필수] 체크박스를 모두 체크해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, UserChooseActivity::class.java)
                startActivity(intent)
            }
        }
    }
}