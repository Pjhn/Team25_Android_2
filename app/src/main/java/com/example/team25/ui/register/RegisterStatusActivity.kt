package com.example.team25.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.team25.databinding.ActivityRegisterStatusBinding
import com.example.team25.ui.login.LoginEntryActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginEntryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
