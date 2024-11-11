package com.kakaotech.team25M.ui.companion

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakaotech.team25M.domain.model.ReservationStatus.*
import com.kakaotech.team25M.ui.status.CompanionCompleteDialog
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.util.DateFormatter
import com.kakaotech.team25M.databinding.ActivityLiveCompanionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
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
        collectRunningReservation()
        collectAccompanyInfoList()
    }

    private fun collectRunningReservation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                liveCompanionViewModel.runningReservation.collectLatest { reservationInfo ->
                    if (reservationInfo == null) {
                        binding.patientNameTextView.visibility = View.GONE
                        binding.managerStatusLayout.visibility = View.GONE
                    } else binding.patientNameTextView.text = reservationInfo.patient.patientName
                }
            }
        }
    }

    private fun collectAccompanyInfoList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                liveCompanionViewModel.accompanyInfoList.collectLatest { accompanyInfoList ->
                    val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)

                    (binding.liveCompanionRecyclerView.adapter as? LiveCompanionRecyclerViewAdapter)?.submitList(
                        accompanyInfoList.map {
                            DateFormatter.formatDate(it.statusDate, outputFormat = dateFormat) +
                                " " + it.statusDescribe
                        }
                    )
                    binding.liveCompanionRecyclerView.visibility =
                        if (accompanyInfoList.isEmpty()) View.GONE else View.VISIBLE
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
        setCompanionCompleteBtnClickListener()
    }

    private fun setCompanionStatusInputFormClickListener() {
        binding.companionStatusInputBtn.setOnClickListener {
            val currentDate = getCurrentFormattedDateTime()
            val statusDescribe = binding.companionStatusInputEditText.text.toString()

            liveCompanionViewModel.reservationId.value?.let { reservationId ->
                liveCompanionViewModel.postAccompanyInfo(
                    reservationId,
                    AccompanyDto("병원 이동", statusDate = currentDate, statusDescribe = statusDescribe)
                )
            }

            collectAccompanyInfoList()
            binding.companionStatusInputEditText.text.clear()
        }
    }

    private fun setCompanionCompleteBtnClickListener() {
        binding.completeCompanionBtn.setOnClickListener {
            val runningReservationInfo = liveCompanionViewModel.runningReservation.value
            val runningReservationId = liveCompanionViewModel.reservationId.value

            if (runningReservationInfo != null && runningReservationId != null) {
                val companionCompleteDialog =
                    CompanionCompleteDialog.newInstance(runningReservationInfo)

                liveCompanionViewModel.changeReservation(runningReservationId, 완료)
                companionCompleteDialog.show(supportFragmentManager, "CompanionCompleteDialog")
            }
        }
    }

    private fun getCurrentFormattedDateTime(): String {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yy.MM.dd HH:mm:ss", Locale.KOREAN)

        return dateFormat.format(currentTime)
    }

    private fun navigateToPrevious() {
        binding.mapPreviousBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
