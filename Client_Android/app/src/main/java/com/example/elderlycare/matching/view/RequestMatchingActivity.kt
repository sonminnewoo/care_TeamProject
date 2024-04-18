package com.example.elderlycare.matching.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.elderlycare.R
import com.example.elderlycare.databinding.MatchingActivityRequestMatchingBinding
import com.example.elderlycare.matching.model.MatchingRequestDto
import com.example.elderlycare.utils.Constants
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar

class RequestMatchingActivity : AppCompatActivity() {

    private lateinit var binding: MatchingActivityRequestMatchingBinding
    private var caregiverId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchingActivityRequestMatchingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 날짜 선택 다이얼로그 설정
        binding.startDate.setOnClickListener { showDatePickerDialog(binding.startDate) }
        binding.endDate.setOnClickListener { showDatePickerDialog(binding.endDate) }

        // 시간 선택 다이얼로그 설정
        binding.startTime.setOnClickListener { showTimePickerDialog(binding.startTime) }
        binding.endTime.setOnClickListener { showTimePickerDialog(binding.endTime) }

        caregiverId = intent.getIntExtra("caregiverId", -1)
        if (caregiverId != -1) {
            fetchCaregiverInfo(caregiverId!!)
        } else {
            binding.caregiverInfo.visibility = View.GONE
        }

        binding.userRoleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        binding.elderlyInfoFields.visibility = View.GONE
                        binding.guardianInfoFields.visibility = View.GONE
                        binding.elderlyInfoFieldsForGuardian.visibility = View.GONE
                    }
                    1 -> {
                        binding.elderlyInfoFields.visibility = View.VISIBLE
                        binding.guardianInfoFields.visibility = View.GONE
                        binding.elderlyInfoFieldsForGuardian.visibility = View.GONE
                    }
                    2 -> {
                        binding.elderlyInfoFields.visibility = View.GONE
                        binding.guardianInfoFields.visibility = View.GONE
                        binding.elderlyInfoFieldsForGuardian.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.elderlyInfoFields.visibility = View.GONE
                binding.guardianInfoFields.visibility = View.GONE
                binding.elderlyInfoFieldsForGuardian.visibility = View.GONE
            }
        }

        binding.hasGuardianSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        binding.guardianInfoFields.visibility = View.GONE
                    }
                    1 -> {
                        binding.guardianInfoFields.visibility = View.VISIBLE
                    }
                    2 -> {
                        binding.guardianInfoFields.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.guardianInfoFields.visibility = View.GONE
            }
        }

        binding.requestButton.setOnClickListener {
            sendMatchingRequest()
        }
    }

    // 날짜 선택 다이얼로그 표시
    private fun showDatePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                textView.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    // 시간 선택 다이얼로그 표시
    private fun showTimePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                textView.text = selectedTime
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

    private fun fetchCaregiverInfo(caregiverId: Int) {
        val url = "${Constants.BASE_URL}/m/matching/request?caregiverId=$caregiverId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val userObject = response.getJSONObject("user")
                    val caregiverObject = response.getJSONObject("caregiver")

                    val caregiverName = userObject.getString("name")
                    val caregiverCountry = userObject.getString("country")
                    val caregiverExperience = caregiverObject.getString("experience")
                    val caregiverCertification = caregiverObject.getString("certification")
                    val caregiverGender = userObject.getString("gender")
                    val caregiverImage = userObject.getString("image")

                    binding.caregiverName.text = caregiverName
                    binding.caregiverCountry.text = caregiverCountry
                    binding.caregiverExperience.text = caregiverExperience
                    binding.caregiverCertification.text = caregiverCertification
                    loadImage(binding.caregiverImage, caregiverImage, caregiverGender)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to parse JSON", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to fetch caregiver info", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun loadImage(imageView: ImageView, imageName: String, gender: String) {
        val imageResourceId = resources.getIdentifier(imageName, "drawable", packageName)
        if (imageResourceId != 0) {
            imageView.setImageResource(imageResourceId)
        } else {
            val defaultImage = if (gender == "Male") R.drawable.defaultmale else R.drawable.defaultfemale
            imageView.setImageResource(defaultImage)
        }
    }
    private fun createMatchingRequestDto(): MatchingRequestDto {
        val userRole = when (binding.userRoleSpinner.selectedItem.toString()) {
            "노인 본인" -> "SENIOR"
            "보호자" -> "GUARDIAN"
            else -> throw IllegalArgumentException("Invalid user role")
        }
        val preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userId = preferences.getLong("user.userId", -1)

        return MatchingRequestDto().apply {
            this.userRole = userRole
            this.userId = userId
            caregiverId = if (this@RequestMatchingActivity.caregiverId == -1) null else this@RequestMatchingActivity.caregiverId?.toLong()
            matchingCountry = binding.matchingCountry.text.toString()
            startDate = binding.startDate.text.toString()
            endDate = binding.endDate.text.toString()
            startTime = binding.startTime.text.toString()
            endTime = binding.endTime.text.toString()

            when (userRole) {
                "SENIOR" -> {
                    health = binding.health.text.toString()
                    requirements = binding.requirements.text.toString()
                    hasGuardian = when (binding.hasGuardianSpinner.selectedItem.toString()) {
                        "있음" -> true
                        "없음" -> false
                        else -> throw IllegalArgumentException("Invalid hasGuardian value")
                    }

                    if (hasGuardian == true) {
                        guardianName = binding.guardianName.text.toString()
                        relationship = binding.relationshipWithGuardian.text.toString()
                    }
                }
                "GUARDIAN" -> {
                    relationship = binding.relationshipWithElder.text.toString()
                    elderlyName = binding.elderlyName.text.toString()
                    elderlyGender = binding.elderlyGenderSpinner.selectedItem.toString()
                    health = binding.health.text.toString()
                    requirements = binding.requirements.text.toString()
                }
            }
        }
    }

    private fun sendMatchingRequest() {
        val url = "${Constants.BASE_URL}/m/matching/request"

        try {
            val matchingRequest = createMatchingRequestDto()
            val jsonString = Gson().toJson(matchingRequest)
            // JSON 데이터 로그 출력
            Log.d("MatchingRequest", jsonString)

            val jsonRequest = JsonObjectRequest(
                Request.Method.POST, url, JSONObject(Gson().toJson(matchingRequest)),
                { response ->
                    // 요청 성공 시 처리
                    try {
                        val message = response.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, FindJobsActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } catch (e: JSONException) {
                        // 응답 본문 파싱 실패 시 처리
                        Toast.makeText(this, "매칭 요청 전송에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                },
                { error ->
                    // 요청 실패 시 처리
                    error.printStackTrace()
                    Toast.makeText(this, "매칭 요청 전송에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            )

            Volley.newRequestQueue(this).add(jsonRequest)
        } catch (e: Exception) {
            // 파싱 오류 처리
            e.printStackTrace()
            Toast.makeText(this, "입력한 데이터의 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}