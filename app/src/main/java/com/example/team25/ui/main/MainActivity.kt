package com.example.team25.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.team25.databinding.ActivityMainBinding
import com.example.team25.ui.companion.LiveCompanionActivity
import com.example.team25.ui.login.LoginEntryActivity
import com.example.team25.ui.login.LoginViewModel
import com.example.team25.ui.status.ReservationStatusActivity
import com.kakao.sdk.common.util.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        navigateToLiveCompanion()
        navigateToReservationStatus()
        setLogoutClickListener()
    }

    private fun setLogoutClickListener() {
        binding.logoutTextView.setOnClickListener {
            loginViewModel.logout()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginEntryActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLiveCompanion() {
        binding.realTimeCompanionManageBtn.setOnClickListener {
            val intent = Intent(this, LiveCompanionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToReservationStatus() {
        binding.reservationManageBtn.setOnClickListener {
            val intent = Intent(this, ReservationStatusActivity::class.java)
            startActivity(intent)
        }
    }
}
