package com.kakaotech.team25M.ui.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.kakaotech.team25M.databinding.FragmentCertificateBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CertificateFragment : Fragment() {
    private var _binding: FragmentCertificateBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCertificateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initImageLauncher()
        loadInfo()
        observeRegistrationStatus()
        navigateToPrevious()
        setRegisterClickListener()
        setPrivacyClickListener()
        setThirdPrivacyClickListener()
    }

    private fun setPrivacyClickListener() {
        binding.detailsButton.setOnClickListener {
            val message = SpannableStringBuilder()
                .bold { append("개인정보 수집 목적:\n") }
                .append("서비스 제공\n\n")
                .bold { append("수집 항목:\n") }
                .append(
                    "이름, 성별, 프로필 이미지, 증명서 이미지\n" +
                        "자기 소개, 경력, 근무 시간, 근무 지역\n\n"
                )
                .bold { append("보유 및 이용 기간:\n") }
                .append("회원 탈퇴 3년 후 파기\n\n")

            AlertDialog.Builder(requireContext())
                .setTitle("개인정보 수집 및 이용 동의")
                .setMessage(message)
                .setPositiveButton("확인", null)
                .show()
        }
    }

    private fun setThirdPrivacyClickListener() {
        binding.thirdDetailsButton.setOnClickListener {
            val message = SpannableStringBuilder()
                // 제공받는 자 (제목)
                .append("제공받는 자:\n")
                // 내용
                .bold { append("메디투게더 APP 이용자\n\n") }

                // 제공받는 자의 이용 목적 (제목)
                .append("제공받는 자의 개인정보 이용 목적:\n")
                // 내용
                .bold { append("서비스 이용 및 관리\n\n") }

                // 제공하는 개인정보 항목 (제목)
                .append("제공하는 개인정보 항목:\n")
                // 내용
                .bold {
                    append(
                        "이름, 성별, 프로필 이미지, 증명서 이미지\n" +
                            "자기 소개, 경력, 근무 시간, 근무 지역\n\n"
                    )
                }

                // 보유 및 이용 기간 (제목)
                .append("제공 받는자의 보유기간:\n")
                // 내용
                .bold { append("회원 탈퇴 3년 후 파기\n") }

            AlertDialog.Builder(requireContext())
                .setTitle("개인정보 제3자 제공 동의")
                .setMessage(message)
                .setPositiveButton("확인", null)
                .show()
        }
    }

    private fun loadInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            registerViewModel.certificateImage.collect { certificateImageUrl ->
                if (certificateImageUrl.isNotEmpty()) {
                    Glide.with(this@CertificateFragment)
                        .load(certificateImageUrl)
                        .into(binding.certificateUploadBtn)
                }
            }
        }
    }

    private fun initImageLauncher() {
        val getImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.certificateUploadBtn.setImageURI(it)
                binding.addImageImageView.visibility = View.GONE
                binding.addImageTextView.visibility = View.GONE

                registerViewModel.updateCertificateImage(it.toString())
            }
        }

        binding.certificateUploadBtn.setOnClickListener {
            getImageLauncher.launch("image/*")
        }
    }

    private fun navigateToPrevious() {
        binding.backBtn.setOnClickListener {
            registerViewModel.logManagerInfo()
            parentFragmentManager.popBackStack()
        }
    }

    private fun setRegisterClickListener() {
        binding.registerFinishBtn.setOnClickListener {
            if (registerViewModel.isCertificateImageEmpty()) {
                Toast.makeText(requireContext(), "증명서 이미지를 등록해주세요.", Toast.LENGTH_SHORT).show()
            } else if(binding.privacyAgreementCheckbox.isChecked && binding.thirdPartyAgreementCheckbox.isChecked) {
                registerViewModel.uploadImage()
                Toast.makeText(requireContext(), "등록 신청 중입니다. 잠시만 기다려주세요.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "모든 항목에 동의해 주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeRegistrationStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            registerViewModel.isRegistered.collect { isSuccess ->
                if (isSuccess) {
                    navigateToRegisterStatusActivity()
                }
            }
        }
    }

    private fun navigateToRegisterStatusActivity() {
        val intent = Intent(requireActivity(), RegisterStatusActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
