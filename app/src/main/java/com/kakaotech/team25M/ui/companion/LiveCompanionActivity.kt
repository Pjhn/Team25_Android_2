package com.kakaotech.team25M.ui.companion

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.util.DateFormatter
import com.kakaotech.team25M.databinding.ActivityLiveCompanionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class LiveCompanionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveCompanionBinding
    private val liveCompanionViewModel: LiveCompanionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLiveCompanionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserves()
        setLiveCompanionRecyclerViewAdapter()
        setClickListener()
        navigateToPrevious()
    }

    private fun setupObserves() {
        collectAccompanyInfo()
        collectRunningReservation()
    }

    private fun collectRunningReservation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                liveCompanionViewModel.runningReservation.collect { reservationInfo ->
                    if (reservationInfo != null) {
                        binding.notCompanionTextView.visibility = View.GONE
                        binding.managerStatusLayout.visibility = View.VISIBLE
                        binding.patientNameTextView.text = reservationInfo.patient.patientName

                        liveCompanionViewModel.updateAccompanyInfo(reservationInfo.reservationId)
                        liveCompanionViewModel.reservationId = reservationInfo.reservationId

                        this.cancel()
                    }
                }
            }
        }
    }

    private fun collectAccompanyInfo() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                liveCompanionViewModel.accompanyInfo.collect { accompanyInfoList ->
                    if (accompanyInfoList.isNullOrEmpty()) binding.liveCompanionRecyclerView.visibility = View.GONE
                    else {
                        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)

                        binding.liveCompanionRecyclerView.visibility = View.VISIBLE

                        (binding.liveCompanionRecyclerView.adapter as? LiveCompanionRecyclerViewAdapter)?.submitList(
                            accompanyInfoList.map {
                                DateFormatter.formatDate(it.statusDate, outputFormat = dateFormat) +
                                    " " + it.statusDescribe
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setLiveCompanionRecyclerViewAdapter() {
        val adapter = LiveCompanionRecyclerViewAdapter()
        binding.liveCompanionRecyclerView.adapter = adapter
        binding.liveCompanionRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setClickListener() {
        setCompanionStatusInputFormClickListener()
    }

    private fun setCompanionStatusInputFormClickListener() {
        binding.companionStatusInputBtn.setOnClickListener {
            val statusDescribe = binding.companionStatusInputEditText.text.toString().trim()

            if (statusDescribe.isEmpty()) {
                Toast.makeText(this, "상태를 입력해 주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentDate =
                DateFormatter.formatDate(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            val runningId = liveCompanionViewModel.reservationId

            if (runningId.isNullOrEmpty()) {
                Toast.makeText(this, "진행 중인 예약이 조회되지 않습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            liveCompanionViewModel.postAccompanyInfo(
                runningId,
                AccompanyDto("병원 이동", statusDate = currentDate, statusDescribe = statusDescribe)
            )

            binding.companionStatusInputEditText.text.clear()
        }
    }

    private fun navigateToPrevious() {
        binding.mapPreviousBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
