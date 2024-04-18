package com.example.elderlycare.matching.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.elderlycare.R
import com.example.elderlycare.utils.Constants
import org.json.JSONException

class CaregiverDetailActivity : AppCompatActivity() {

    private lateinit var caregiverImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var experienceTextView: TextView
    private lateinit var certificationTextView: TextView
    private lateinit var specializationTextView: TextView
    private lateinit var availableHoursTextView: TextView
    private lateinit var requestButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matching_activity_caregiver_detail)

        val caregiverId = intent.getIntExtra("caregiverId", -1)

        caregiverImageView = findViewById(R.id.caregiver_image)
        nameTextView = findViewById(R.id.name_textview)
        countryTextView = findViewById(R.id.country_textview)
        experienceTextView = findViewById(R.id.experience_textview)
        certificationTextView = findViewById(R.id.certification_textview)
        specializationTextView = findViewById(R.id.specialization_textview)
        availableHoursTextView = findViewById(R.id.available_hours_textview)
        requestButton = findViewById(R.id.request_button)

        fetchCaregiverDetails(caregiverId)
    }

    private fun fetchCaregiverDetails(caregiverId: Int) {
        val url = "${Constants.BASE_URL}/m/matching/caregivers/$caregiverId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val userObject = response.getJSONObject("user")
                    val caregiverObject = response.getJSONObject("caregiver")

                    val name = userObject.getString("name")
                    val country = userObject.getString("country")
                    val experience = caregiverObject.getString("experience")
                    val experienceYears = caregiverObject.getInt("experienceYears")
                    val certification = caregiverObject.getString("certification")
                    val specialization = caregiverObject.getString("specialization")
                    val availableHours = caregiverObject.getString("availableHours")
                    val gender = userObject.getString("gender")
                    val image = userObject.getString("image")

                    // 이미지 로드
                    val imageResourceId = resources.getIdentifier(image, "drawable", packageName)
                    if (imageResourceId != 0) {
                        caregiverImageView.setImageResource(imageResourceId)
                    } else {
                        val defaultImage = if (gender == "Male") R.drawable.defaultmale else R.drawable.defaultfemale
                        caregiverImageView.setImageResource(defaultImage)
                    }

                    nameTextView.text = name
                    countryTextView.text = country
                    experienceTextView.text = "${experience}, ${experienceYears}년"
                    certificationTextView.text = certification
                    specializationTextView.text = specialization
                    availableHoursTextView.text = availableHours

                    // 요청 버튼 클릭 리스너 설정
                    requestButton.setOnClickListener {
                        val intent = Intent(this, RequestMatchingActivity::class.java)
                        intent.putExtra("caregiverId", caregiverId)
                        startActivity(intent)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to parse JSON", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to fetch caregiver details", Toast.LENGTH_SHORT).show()
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }
}