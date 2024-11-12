package com.kakaotech.team25M.ui.status

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReservationStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationStatusBinding
    private val reservationStatusViewModel: ReservationStatusViewModel by viewModels()

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
                reservationStatusViewModel.changeReservation(reservationInfo.reservationId, 진행중)
            }

            override fun onCompleteClicked(reservationInfo: ReservationInfo) {
                val companionCompleteDialog =
                    CompanionCompleteDialog.newInstance(reservationInfo)

                reservationStatusViewModel.changeReservation(reservationInfo.reservationId, 완료)
                companionCompleteDialog.show(supportFragmentManager, "CompanionCompleteDialog")
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
                    Intent(this@ReservationStatusActivity, ReservationReportActivity::class.java)
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
                reservationStatusViewModel.changeReservation(item.reservationId, 확정)
                reservationStatusViewModel.updateReservations()
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

    private fun navigateToPrevious() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
