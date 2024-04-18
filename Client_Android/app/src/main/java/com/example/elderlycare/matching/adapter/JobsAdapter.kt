package com.example.elderlycare.matching.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderlycare.R
import com.example.elderlycare.matching.model.Matching
import com.example.elderlycare.matching.view.MatchingDetailActivity


class JobsAdapter(
    private val context: Context,
    private val jobList: List<Matching>
) : RecyclerView.Adapter<JobsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.matching_item_matching, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobList[position]
        holder.bind(job)

        // 매칭 버튼 가시성 제어
        if (job.status == "POSTED" && isUserCaregiver()) {
            holder.matchingButton.visibility = View.VISIBLE
        } else {
            holder.matchingButton.visibility = View.GONE
        }
    }

    private fun isUserCaregiver(): Boolean {
        // SharedPreferences에서 사용자 역할 가져오기
        val preferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val userRole = preferences.getString("user.role", "")
        return userRole == "CAREGIVER"
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val matchingCountryTextView: TextView = itemView.findViewById(R.id.matching_country_textview)
        private val periodTextView: TextView = itemView.findViewById(R.id.period_textview)
        private val timeTextView: TextView = itemView.findViewById(R.id.time_textview)
        private val statusTextView: TextView = itemView.findViewById(R.id.status_textview)
        private val detailsButton: Button = itemView.findViewById(R.id.details_button)
        val matchingButton: Button = itemView.findViewById(R.id.matching_button)

        fun bind(job: Matching) {
            matchingCountryTextView.text = job.matchingCountry
            periodTextView.text = context.getString(R.string.period_text, job.startDate, job.endDate)
            timeTextView.text = context.getString(R.string.time_text, job.startTime, job.endTime)
            statusTextView.text = getStatusText(job.status)

            detailsButton.setOnClickListener {
                val intent = Intent(context, MatchingDetailActivity::class.java)
                intent.putExtra("matchingId", job.id)
                context.startActivity(intent)
            }

            matchingButton.setOnClickListener {
                //Todo.매칭 수락 버튼 기능 구현
            }
        }

        private fun getStatusText(status: String): String {
            return when (status) {
                "POSTED" -> "구직 중"
                "REQUESTED" -> "요청 중"
                "IN_PROGRESS" -> "진행 중"
                "COMPLETED" -> "완료됨"
                "CANCELLED" -> "취소됨"
                else -> "?"
            }
        }
    }
}