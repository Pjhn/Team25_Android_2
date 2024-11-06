package com.example.team25.ui.profile

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.team25.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val managerInformationViewModel: ManagerInformationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeProfileLoadStatus()
        managerInformationViewModel.getProfile()

        navigateToEditWorkLocationActivity()
    }

    private fun observeProfileLoadStatus() {
        lifecycleScope.launch {
            managerInformationViewModel.profileLoadStatus.collect { status ->
                when (status) {
                    ManagerInformationViewModel.ProfileLoadStatus.LOADING -> {
                        binding.loadingTextView.text = "로딩 중입니다..."
                    }
                    ManagerInformationViewModel.ProfileLoadStatus.SUCCESS -> {
                        binding.loadingTextView.visibility = View.GONE
                        binding.managerInforLayout.visibility = View.VISIBLE
                    }
                    ManagerInformationViewModel.ProfileLoadStatus.FAILURE -> {
                        binding.loadingTextView.text = "프로필 조회에 실패했습니다."
                    }
                }
            }
        }
    }

    private fun navigateToEditWorkLocationActivity() {
        binding.editWorkTimeLocationBtn.setOnClickListener {
            val intent = Intent(this, EditWorkLocationActivity::class.java)
            startActivity(intent)
        }
    }
}
