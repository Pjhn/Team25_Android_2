package com.kakaotech.team25M.ui.status

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.kakaotech.team25M.domain.model.ReservationStatus.*
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.ui.status.adapter.CompanionCompleteHistoryRecyclerViewAdapter
import com.kakaotech.team25M.ui.status.adapter.ReservationApplyRecyclerViewAdapter
import com.kakaotech.team25M.ui.status.adapter.ReservationStatusRecyclerViewAdapter
import com.kakaotech.team25M.ui.status.interfaces.OnCompanionStartClickListener
import com.kakaotech.team25M.ui.status.interfaces.OnReservationApplyClickListener
import com.kakaotech.team25M.ui.status.interfaces.OnShowDetailsClickListener
import com.kakaotech.team25M.databinding.ActivityReservationStatusBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReservationStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationStatusBinding
    private val reservationStatusViewModel: ReservationStatusViewModel by viewModels()
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val reservationId = result.data?.getStringExtra("reservationId")

            if (reservationId != null) {
                reservationStatusViewModel.changeReservation(reservationId, 완료)
            } else {
                Toast.makeText(this, "동행완료 처리 오류입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReservationStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToPrevious()
        setReservationStatusRecyclerViewAdapter()
        setReservationApplyRecyclerViewAdapter()
        setCompanionCompleteHistoryRecyclerViewAdapter()
        setObserves()
    }

    override fun onStart() {
        super.onStart()
        reservationStatusViewModel.updateReservations()
    }

    private fun setObserves() {
        collectConfirmedOrRunningReservations()
        collectPendingReservations()
        collectCompletedReservations()
        collectReservations()
    }

    private fun collectReservations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reservationStatusViewModel.reservations.collect { reservations ->
                    reservationStatusViewModel.updateConfirmedOrRunningReservations(reservations)
                    reservationStatusViewModel.updatePendingReservations(reservations)
                    reservationStatusViewModel.updateCompletedReservations(reservations)
                }
            }
        }
    }

    private fun collectConfirmedOrRunningReservations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reservationStatusViewModel.confirmedOrRunningReservations.collect { reservations ->
                    (binding.reservationStatusRecyclerView.adapter as? ReservationStatusRecyclerViewAdapter)
                        ?.submitList(reservations)
                }
            }
        }
    }

    private fun collectPendingReservations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reservationStatusViewModel.pendingReservations.collect { reservations ->
                    (binding.reservationApplyRecyclerView.adapter as? ReservationApplyRecyclerViewAdapter)
                        ?.submitList(reservations)
                }
            }
        }
    }

    private fun collectCompletedReservations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reservationStatusViewModel.completedReservations.collect { reservations ->
                    (binding.companionCompleteHistoryRecyclerView.adapter as? CompanionCompleteHistoryRecyclerViewAdapter)
                        ?.submitList(reservations)
                }
            }
        }
    }

    private fun setReservationStatusRecyclerViewAdapter() {
        val companionStartClickListener = object : OnCompanionStartClickListener {
            override fun onStartClicked(reservationInfo: ReservationInfo) {
                showStartDialog(reservationInfo.reservationId)
            }

            override fun onCompleteClicked(reservationInfo: ReservationInfo) {
                showEndDialog(reservationInfo)
            }
        }

        val detailsClickListener = object : OnShowDetailsClickListener {
            override fun onDetailsClicked(item: ReservationInfo) {
                val intent =
                    Intent(this@ReservationStatusActivity, ReservationDetailsActivity::class.java)
                        .putExtra("ReservationInfo", item)
                startActivity(intent)
            }
        }

        val adapter =
            ReservationStatusRecyclerViewAdapter(
                companionStartClickListener,
                detailsClickListener
            )

        binding.reservationStatusRecyclerView.adapter = adapter
        binding.reservationStatusRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setCompanionCompleteHistoryRecyclerViewAdapter() {
        val onShowDetailsClickListener = object : OnShowDetailsClickListener {
            override fun onDetailsClicked(item: ReservationInfo) {
                val intent =
                    Intent(this@ReservationStatusActivity, ReservationDetailsActivity::class.java)
                        .putExtra("ReservationInfo", item)
                startActivity(intent)
            }
        }
        val adapter = CompanionCompleteHistoryRecyclerViewAdapter(onShowDetailsClickListener)

        binding.companionCompleteHistoryRecyclerView.adapter = adapter
        binding.companionCompleteHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setReservationApplyRecyclerViewAdapter() {
        val onReservationApplyClickListener = object : OnReservationApplyClickListener {
            override fun onAcceptClicked(item: ReservationInfo) {
                showAcceptDialog(item.reservationId)
            }

            override fun onRefuseClicked(item: ReservationInfo) {
                val intent =
                    Intent(this@ReservationStatusActivity, ReservationRejectActivity::class.java)
                intent.putExtra("ReservationInfo", item)
                startActivity(intent)
            }
        }

        val onShowDetailsClickListener = object : OnShowDetailsClickListener {
            override fun onDetailsClicked(item: ReservationInfo) {
                val intent =
                    Intent(this@ReservationStatusActivity, ReservationDetailsActivity::class.java)
                intent.putExtra("ReservationInfo", item)
                startActivity(intent)
            }
        }

        val adapter = ReservationApplyRecyclerViewAdapter(
            onReservationApplyClickListener,
            onShowDetailsClickListener
        )

        binding.reservationApplyRecyclerView.adapter = adapter
        binding.reservationApplyRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun showAcceptDialog(reservationId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("예약 수락")
        builder.setMessage("예약을 수락하시겠습니까?")
        builder.setPositiveButton("확인") { _, _ ->
            reservationStatusViewModel.changeReservation(reservationId, 확정)
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showStartDialog(reservationId: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("동행 시작")
        builder.setMessage("동행을 시작하시겠습니까?")
        builder.setPositiveButton("확인") { _, _ ->
            reservationStatusViewModel.changeReservation(reservationId, 진행중)
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showEndDialog(reservationInfo: ReservationInfo) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("동행 완료")
        builder.setMessage("동행을 완료하고 리포트를 작성하시겠습니까?")
        builder.setPositiveButton("확인") { _, _ ->
            navigateToWriteReport(reservationInfo)
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun navigateToWriteReport(reservationInfo: ReservationInfo) {
        val intent = Intent(this, ReservationReportActivity::class.java).apply {
            putExtra("ReservationInfo", reservationInfo)
        }
        startForResult.launch(intent)
    }

    private fun navigateToPrevious() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
