package com.example.elderlycare.user.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.elderlycare.adapter.SliderAdapter
import com.example.elderlycare.databinding.UserChooseBinding
class UserChooseActivity : AppCompatActivity() {


    private lateinit var viewPager: ViewPager
    private lateinit var sliderAdapter: SliderAdapter

    private lateinit var binding: UserChooseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding
        binding = UserChooseBinding.inflate(layoutInflater)
        // Set the content view to the root of the binding
        setContentView(binding.root)

        // Set click listeners on the buttons using the binding
        binding.btnSenior.setOnClickListener {
            navigateToDetails("senior")
        }

        binding.btnCaregiver.setOnClickListener {
            navigateToDetails("caregiver")
        }
    }

    private fun navigateToDetails(userType: String) {
        val intent = Intent(this, UserRegisterActivity::class.java).apply {
            putExtra("userType", userType)
        }
        startActivity(intent)
    }
}
