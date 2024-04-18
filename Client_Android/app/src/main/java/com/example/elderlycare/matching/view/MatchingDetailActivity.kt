package com.example.elderlycare.matching.view

import android.os.Bundle
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


class MatchingDetailActivity : AppCompatActivity() {

    private lateinit var caregiverNameTextView: TextView
    private lateinit var caregiverCountryTextView: TextView
    private lateinit var caregiverExperienceTextView: TextView
    private lateinit var caregiverCertificationTextView: TextView
    private lateinit var caregiverImageView: ImageView
    private lateinit var seniorNameTextView: TextView
    private lateinit var seniorHealthTextView: TextView
    private lateinit var seniorRequirementsTextView: TextView
    private lateinit var seniorImageView: ImageView
    private lateinit var startDateTextView: TextView
    private lateinit var endDateTextView: TextView
    private lateinit var startTimeTextView: TextView
    private lateinit var endTimeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matching_activity_matching_detail)

        val matchingId = intent.getIntExtra("matchingId", -1)

        caregiverNameTextView = findViewById(R.id.caregiver_name_textview)
        caregiverCountryTextView = findViewById(R.id.caregiver_country_textview)
        caregiverExperienceTextView = findViewById(R.id.caregiver_experience_textview)
        caregiverCertificationTextView = findViewById(R.id.caregiver_certification_textview)
        caregiverImageView = findViewById(R.id.caregiver_image)
        seniorNameTextView = findViewById(R.id.senior_name_textview)
        seniorHealthTextView = findViewById(R.id.senior_health_textview)
        seniorRequirementsTextView = findViewById(R.id.senior_requirements_textview)
        seniorImageView = findViewById(R.id.senior_image)
        startDateTextView = findViewById(R.id.start_date_textview)
        endDateTextView = findViewById(R.id.end_date_textview)
        startTimeTextView = findViewById(R.id.start_time_textview)
        endTimeTextView = findViewById(R.id.end_time_textview)

        fetchMatchingDetail(matchingId)
    }

    private fun fetchMatchingDetail(matchingId: Int) {
        val url = "${Constants.BASE_URL}/m/matching/jobs/$matchingId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val caregiverUser = response.getJSONObject("caregiverUser")
                    val caregiver = response.getJSONObject("caregiver")
                    val seniorUser = response.getJSONObject("seniorUser")
                    val senior = response.getJSONObject("senior")
                    val matching = response.getJSONObject("matching")

                    val caregiverName = caregiverUser.getString("name")
                    val caregiverCountry = caregiverUser.getString("country")
                    val caregiverExperience = caregiver.getString("experience")
                    val caregiverCertification = caregiver.getString("certification")
                    val caregiverGender = caregiverUser.getString("gender")
                    val caregiverImage = caregiverUser.getString("image")

                    val seniorName = seniorUser.getString("name")
                    val seniorHealth = senior.getString("health")
                    val seniorRequirements = senior.getString("requirements")
                    val seniorGender = seniorUser.getString("gender")
                    val seniorImage = seniorUser.getString("image")

                    val startDate = matching.getString("startDate")
                    val endDate = matching.getString("endDate")
                    val startTime = matching.getString("startTime")
                    val endTime = matching.getString("endTime")

                    caregiverNameTextView.text = caregiverName
                    caregiverCountryTextView.text = caregiverCountry
                    caregiverExperienceTextView.text = caregiverExperience
                    caregiverCertificationTextView.text = caregiverCertification
                    loadImage(caregiverImageView, caregiverImage, caregiverGender)

                    seniorNameTextView.text = seniorName
                    seniorHealthTextView.text = seniorHealth
                    seniorRequirementsTextView.text = seniorRequirements
                    loadImage(seniorImageView, seniorImage, seniorGender)

                    startDateTextView.text = startDate
                    endDateTextView.text = endDate
                    startTimeTextView.text = startTime
                    endTimeTextView.text = endTime
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to parse JSON", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to fetch matching detail", Toast.LENGTH_SHORT).show()
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
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
}