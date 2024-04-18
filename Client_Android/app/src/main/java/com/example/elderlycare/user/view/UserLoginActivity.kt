package com.example.elderlycare.user.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.ServerError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.elderlycare.MainActivity
import com.example.elderlycare.R
import com.example.elderlycare.utils.Constants
import org.json.JSONObject

class UserLoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_activity_user_login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // 로그인 요청 보내기
            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        val url = "${Constants.BASE_URL}/m/user/login"

        val requestBody = JSONObject()
        requestBody.put("email", email)
        requestBody.put("password", password)

        val request = JsonObjectRequest(
            Request.Method.POST, url, requestBody,
            { response ->
                // 로그인 성공 처리
                val userEmail = response.getString("email")
                val userRole = response.getString("role")
                val userId = response.getLong("userId")

                // 사용자 정보 저장 (예: SharedPreferences)
                val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("user.email", userEmail)
                editor.putString("user.role", userRole)
                editor.putLong("user.userId", userId)
                editor.apply()
                Toast.makeText(this@UserLoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()


                // 메인 액티비티로 이동
                val intent = Intent(this@UserLoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            },
            { error ->
                // 로그인 실패 처리
                val errorMessage = when (error) {
                    is NetworkError -> "네트워크 오류가 발생했습니다."
                    is ServerError -> "서버 오류가 발생했습니다."
                    is AuthFailureError -> "이메일 또는 비밀번호를 확인해주세요."
                    is ParseError -> "응답 데이터 파싱 오류가 발생했습니다."
                    else -> "로그인 실패"
                }
                AlertDialog.Builder(this@UserLoginActivity)
                    .setMessage(errorMessage)
                    .setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}