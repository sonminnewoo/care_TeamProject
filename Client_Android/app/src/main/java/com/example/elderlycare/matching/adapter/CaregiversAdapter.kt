package com.example.elderlycare.matching.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elderlycare.R
import com.example.elderlycare.matching.model.Caregiver
import com.example.elderlycare.matching.view.RequestMatchingActivity

class CaregiversAdapter(
    private val context: Context,
    private val caregiverList: MutableList<Caregiver>,
    private val onItemClick: (Caregiver) -> Unit
) : RecyclerView.Adapter<CaregiversAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.matching_item_caregiver, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val caregiver = caregiverList[position]
        holder.bind(caregiver)
    }

    override fun getItemCount(): Int {
        return caregiverList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.caregiver_image)
        private val nameTextView: TextView = itemView.findViewById(R.id.name_textview)
        private val countryTextView: TextView = itemView.findViewById(R.id.country_textview)
        private val experienceTextView: TextView = itemView.findViewById(R.id.experience_textview)
        private val certificationTextView: TextView = itemView.findViewById(R.id.certification_textview)
        private val availableHoursTextView: TextView = itemView.findViewById(R.id.available_hours_textview)
        private val detailsButton: Button = itemView.findViewById(R.id.details_button)
        private val requestButton: Button = itemView.findViewById(R.id.request_button)

        fun bind(caregiver: Caregiver) {
            val imageName = caregiver.image
            val imageResourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            if (imageResourceId != 0) {
                imageView.setImageResource(imageResourceId)
            } else {
                val defaultImage = if (caregiver.gender == "Male") R.drawable.defaultmale else R.drawable.defaultfemale
                imageView.setImageResource(defaultImage)
            }

            nameTextView.text = caregiver.name
            countryTextView.text = caregiver.country
            experienceTextView.text = caregiver.experience
            certificationTextView.text = caregiver.certification
            availableHoursTextView.text = caregiver.availableHours

            detailsButton.setOnClickListener {
                onItemClick(caregiver)
            }

            requestButton.setOnClickListener {
                val intent = Intent(context, RequestMatchingActivity::class.java)
                intent.putExtra("caregiverId", caregiver.caregiverId)
                context.startActivity(intent)
            }
        }
    }
}