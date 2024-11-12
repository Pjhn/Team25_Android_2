package com.kakaotech.team25M.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kakaotech.team25M.databinding.ActivityRegisterStatusBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}
