package com.example.elderlycare.matching.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.elderlycare.R
import com.example.elderlycare.matching.adapter.JobsAdapter
import com.example.elderlycare.matching.model.Matching
import com.example.elderlycare.utils.Constants
import org.json.JSONException

class FindJobsActivity : AppCompatActivity() {

    private lateinit var jobsRecyclerView: RecyclerView
    private val jobList = mutableListOf<Matching>()
    private lateinit var jobsAdapter: JobsAdapter

    private var currentPage = 0
    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matching_activity_find_jobs)

        jobsRecyclerView = findViewById(R.id.jobs_recyclerview)
        jobsAdapter = JobsAdapter(this, jobList)
        jobsRecyclerView.adapter = jobsAdapter
        jobsRecyclerView.layoutManager = LinearLayoutManager(this)

        jobsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && !isLastPage && lastVisibleItem == totalItemCount - 1) {
                    currentPage++
                    fetchJobs(currentPage)
                }
            }
        })

        val requestButton = findViewById<Button>(R.id.request_button)
        requestButton.setOnClickListener {
            val intent = Intent(this, RequestMatchingActivity::class.java)
            startActivity(intent)
        }

        fetchJobs(currentPage)
    }

    private fun fetchJobs(page: Int) {
        isLoading = true
        val url = "${Constants.BASE_URL}/m/matching/jobs?page=$page"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val contentArray = response.getJSONArray("content")
                    for (i in 0 until contentArray.length()) {
                        val jobObject = contentArray.getJSONObject(i)
                        val id = jobObject.getInt("id")
                        val seniorId = jobObject.optInt("seniorId", -1)
                        val caregiverId = jobObject.optInt("caregiverId", -1)
                        val matchingCountry = jobObject.getString("matchingCountry")
                        val startDate = jobObject.getString("startDate")
                        val endDate = jobObject.getString("endDate")
                        val startTime = jobObject.getString("startTime")
                        val endTime = jobObject.getString("endTime")
                        val status = jobObject.getString("status")

                        val job = Matching(
                            id, seniorId, caregiverId, matchingCountry,
                            startDate, endDate, startTime, endTime, status
                        )
                        jobList.add(job)
                    }

                    isLoading = false
                    jobsAdapter.notifyDataSetChanged()

                    val totalPages = response.getInt("totalPages")
                    isLastPage = page == totalPages - 1
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to parse JSON", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to fetch jobs", Toast.LENGTH_SHORT).show()
            }
        )

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentPage", currentPage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentPage = savedInstanceState.getInt("currentPage")
    }

    override fun onResume() {
        super.onResume()
        fetchJobs(currentPage)
    }

}