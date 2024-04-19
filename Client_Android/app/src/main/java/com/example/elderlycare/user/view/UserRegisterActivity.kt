package com.example.elderlycare.user.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.elderlycare.R
import com.example.elderlycare.utils.Constants
import org.json.JSONObject

class UserRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_register)

//        옵션 선택할때 표시하는 부분
        setupSpinnerCountry()
        setupSpinnerGender()

        val userType = intent.getStringExtra("userType") ?: ""
        setupViewsVisibility(userType)

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            val userFormDto = collectUserData(userType)
            if (userFormDto != null) {
                registerUser(userFormDto)
            }
        }
    }
    private fun collectUserData(userType: String): JSONObject? {
        val name = findViewById<EditText>(R.id.editTextName).text.toString()
        val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
        val confirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword).text.toString()
        val phoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber).text.toString()
        val address = findViewById<EditText>(R.id.editTextAddress).text.toString()
        val country = findViewById<Spinner>(R.id.spinnerCountry).selectedItem.toString()
        val gender = findViewById<Spinner>(R.id.spinnerGender).selectedItem.toString()

        // Validate fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            phoneNumber.isEmpty() || address.isEmpty() || country == "지역을 선택해주세요" || gender == "성별을 선택해주세요") {
            Toast.makeText(this, "내용을 입력해주세요 ", Toast.LENGTH_SHORT).show()
            return null // Return null to indicate error
        }

        val data = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
            put("confirmPassword", confirmPassword)
            put("phoneNumber", phoneNumber)
            put("address", address)
            put("country", country)
            put("gender", gender)
            put("userType", userType)
        }

        // 유저 타입에 따른 추가 정보 처리
        when (userType) {
            "senior" -> {
                val health = findViewById<EditText>(R.id.health).text.toString()
                val requirements = findViewById<EditText>(R.id.requirements).text.toString()
                val hasGuardian = findViewById<CheckBox>(R.id.hasGuardian).isChecked
                if (health.isEmpty() || requirements.isEmpty()) {
                    Toast.makeText(this,"내용을 입력해주세요 ", Toast.LENGTH_SHORT).show()
                    return null
                }
                data.put("health", health)
                data.put("requirements", requirements)
                data.put("hasGuardian", hasGuardian)

                if (findViewById<CheckBox>(R.id.hasGuardian).isChecked) {
                    val guardianName = findViewById<EditText>(R.id.guardianName).text.toString()
                    val guardianPhoneNumber = findViewById<EditText>(R.id.guardianNumber).text.toString()
                    val relationship = findViewById<EditText>(R.id.relationship).text.toString()
                    if (guardianName.isEmpty() || guardianPhoneNumber.isEmpty() || relationship.isEmpty()) {
                        Toast.makeText(this,"내용을 입력해주세요 ", Toast.LENGTH_SHORT).show()
                        return null
                    }
                    data.put("guardianName", guardianName)
                    data.put("guardianPhoneNumber", guardianPhoneNumber)
                    data.put("relationship", relationship)
                }
            }
            "caregiver" -> {
                val certification = findViewById<EditText>(R.id.certification).text.toString()
                val specialization = findViewById<EditText>(R.id.specialization).text.toString()
                val experience = findViewById<EditText>(R.id.experience).text.toString()
                val experienceYears = findViewById<EditText>(R.id.experienceYears).text.toString().toInt()
                val availableHours = findViewById<EditText>(R.id.availableHours).text.toString()
                if (certification.isEmpty() || specialization.isEmpty() || experience.isEmpty() || availableHours.isEmpty() ) {
                    Toast.makeText(this,"내용을 입력해주세요 ", Toast.LENGTH_SHORT).show()
                    return null
                }

                data.put("certification", certification)
                data.put("specialization", specialization)
                data.put("experience",experience)
                data.put("experienceYears", experienceYears)
                data.put("availableHours", availableHours)
            }
        }

        return data
    }
    private fun registerUser(userFormData: JSONObject) {
        val queue = Volley.newRequestQueue(this)
        val url = "${Constants.BASE_URL}/m/user/register"

        val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, userFormData,
            { response ->
                // 성공 응답 처리, response 객체에서 메시지 추출하여 Toast 메시지로 보여줌
                val successMessage = response.optString("message", "Registration Successful") // 기본 메시지는 "Registration Successful"
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
            },
            { error ->
                // 에러 처리
                val errorMessage = if (error.networkResponse?.data != null) {
                    // 서버로부터 반환된 에러 메시지 추출
                    String(error.networkResponse.data, Charsets.UTF_8)
                } else {
                    // 네트워크 에러 등 다른 에러의 경우, 기본 에러 메시지 사용
                    "Registration Failed: ${error.message}"
                }
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        queue.add(jsonObjectRequest)
    }

    private fun setupViewsVisibility(userType: String) {
        // Example for senior; adjust similarly for other user types
        val seniorInfoView = findViewById<LinearLayout>(R.id.seniorInfo)
        val guardianInfoView = findViewById<LinearLayout>(R.id.guardianInfo)
        val caregiverInfoView = findViewById<LinearLayout>(R.id.caregiverInfo)

        seniorInfoView.visibility = if (userType == "senior") View.VISIBLE else View.GONE
        guardianInfoView.visibility = View.GONE // Default to GONE, will be made VISIBLE based on CheckBox
        caregiverInfoView.visibility = if (userType == "caregiver") View.VISIBLE else View.GONE

        // CheckBox for Guardian visibility toggle
        val hasGuardianCheckBox = findViewById<CheckBox>(R.id.hasGuardian)
        hasGuardianCheckBox.setOnCheckedChangeListener { _, isChecked ->
            guardianInfoView.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
    }
        private fun setupSpinnerCountry() {
        val countries = arrayOf("지역을 선택해주세요", "서울", "부산", "제주도", "인천", "대구", "광주", "대전", "울산", "세종", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countries)
        val spinnerCountry = findViewById<Spinner>(R.id.spinnerCountry)
        spinnerCountry.adapter = adapter
    }

    private fun setupSpinnerGender() {
        val genders = arrayOf("성별을 선택해주세요", "남자", "여자")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genders)
        val spinnerGender = findViewById<Spinner>(R.id.spinnerGender)
        spinnerGender.adapter = adapter
    }
}
