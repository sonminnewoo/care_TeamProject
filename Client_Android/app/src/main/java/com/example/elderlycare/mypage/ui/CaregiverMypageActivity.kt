package com.example.elderlycare.mypage.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.elderlycare.R
import com.example.elderlycare.board.ui.ListActivity
import com.example.elderlycare.matching.view.FindCaregiversActivity
import com.example.elderlycare.matching.view.FindJobsActivity
import com.example.elderlycare.MainActivity
import com.example.elderlycare.databinding.ActivityCaregiverMypageBinding
import com.google.android.material.tabs.TabLayout

class CaregiverMypageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCaregiverMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaregiverMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabLayout()
        setSupportActionBar(binding.toolbar)
    }

    private fun setupTabLayout() {
        val tabLayout = binding.tabs
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val transaction = supportFragmentManager.beginTransaction()
                when (tab?.position) {
                    0 -> transaction.replace(R.id.tabContent, CaregiverMyInfoFragment())
                    1 -> transaction.replace(R.id.tabContent, MatchingInfoFragment2())
                }
                transaction.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Not used here
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Not used here
            }
        })

        // Set default tab or initial fragment if needed
        supportFragmentManager.beginTransaction().add(R.id.tabContent, CaregiverMyInfoFragment()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        title = ""
        menuInflater.inflate(R.menu.mypage_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.goHome -> startActivity(Intent(this, MainActivity::class.java))
            R.id.goGetCare -> startActivity(Intent(this, FindCaregiversActivity::class.java))
            R.id.goGetJob -> startActivity(Intent(this, FindJobsActivity::class.java))
            R.id.goBoard -> startActivity(Intent(this, ListActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
