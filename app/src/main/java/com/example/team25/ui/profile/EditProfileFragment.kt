package com.example.team25.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.team25.R
import com.example.team25.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private val managerInformationViewModel: ManagerInformationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        observeProfileLoadStatus()
        managerInformationViewModel.getProfile()

        observeProfileImage()
        navigateToEditWorkLocation()
        navigateToEditWorkTime()
        navigateToEditComment()
    }

    private fun observeProfileImage() {
        lifecycleScope.launch {
            managerInformationViewModel.profileImageUri.collect { uri ->
                uri?.let {
                    binding.profileImgeView.setImageURI(it)
                }
            }
        }
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

    private fun navigateToEditWorkTime() {
        binding.editWorkTimeBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, EditWorkTimeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun navigateToEditWorkLocation() {
        binding.locationEditBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, EditWorkLocationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun navigateToEditComment() {
        binding.introductionEditBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, EditCommentFragment())
                .addToBackStack(null)
                .commit()
        }
    }


}
