package com.example.team25.ui.status

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.team25.data.network.services.LocationUpdateService
import com.example.team25.databinding.ActivityReservationStatusBinding
import com.example.team25.domain.model.ReservationInfo
import com.example.team25.domain.model.ReservationStatus.*
import com.example.team25.ui.status.utils.RequestPermission.isPermissionSetGranted
import com.example.team25.ui.status.adapter.CompanionCompleteHistoryRecyclerViewAdapter
import com.example.team25.ui.status.adapter.ReservationApplyRecyclerViewAdapter
import com.example.team25.ui.status.adapter.ReservationStatusRecyclerViewAdapter
import com.example.team25.ui.status.interfaces.OnCompanionStartClickListener
import com.example.team25.ui.status.interfaces.OnReservationApplyClickListener
import com.example.team25.ui.status.interfaces.OnShowDetailsClickListener
import com.example.team25.ui.status.utils.RequestPermission
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.entries.all { it.value }
        if (isGranted) {
            showNotificationPermissionDialog()
        } else {
            Toast.makeText(this, "위치 권한을 설정하세요. 설정에서 변경 가능합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     *  위치 권한이 허용 되어 있을 때 백그라운드 위치 권한 요청 가능
     */
    private val requestBackgroundLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "권한이 설정 되었습니다.\n동행 시작 버튼을 눌러 서비스를 시작해 주세요.", Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(this, "백그라운드 위치 권한을 허용해주세요. 설정에서 변경 가능합니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkLocationSettingsAndStartService(reservationId: String) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000L
        ).build()

        val locationSettingRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val settingClient = LocationServices.getSettingsClient(this)
        val task = settingClient.checkLocationSettings(locationSettingRequest)

        task.addOnSuccessListener {
            startLocationService()
            reservationStatusViewModel.updateRunningReservationId(reservationId)
            reservationStatusViewModel.changeReservation(reservationId, 진행중)
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(
                        "ReservationStatusActivity", "${e.message}"
                    )
                }
            }
        }
    }

    private fun startLocationService() {
        val intent =
            Intent(this@ReservationStatusActivity, LocationUpdateService::class.java)
        this@ReservationStatusActivity.startForegroundService(intent)
    }

    private fun stopLocationService() {
        val intent = Intent(this, LocationUpdateService::class.java)
        this.stopService(intent)
    }

    private fun requestRequiredPermission() {
        val permissions = RequestPermission.getRequiredPermissions()
        requestPermissionLauncher.launch(permissions)
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestBackgroundLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    private fun setObserves() {
        collectConfirmedOrRunningReservations()
        collectPendingReservations()
        collectCompletedReservations()
    }

    private fun collectConfirmedOrRunningReservations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reservationStatusViewModel.confirmedOrRunningReservations.collectLatest { reservations ->
                    (binding.reservationStatusRecyclerView.adapter as? ReservationStatusRecyclerViewAdapter)
                        ?.submitList(reservations)
                }
            }
        }
    }

    private fun collectPendingReservations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reservationStatusViewModel.pendingReservations.collectLatest { reservations ->
                    (binding.reservationApplyRecyclerView.adapter as? ReservationStatusRecyclerViewAdapter)
                        ?.submitList(reservations)
                }
            }
        }
    }

    private fun collectCompletedReservations() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                reservationStatusViewModel.completedReservations.collectLatest { reservations ->
                    (binding.companionCompleteHistoryRecyclerView.adapter as? ReservationStatusRecyclerViewAdapter)
                        ?.submitList(reservations)
                }
            }
        }
    }

    private fun setReservationStatusRecyclerViewAdapter() {
        val companionStartClickListener = object : OnCompanionStartClickListener {
            override fun onStartClicked(reservationInfo: ReservationInfo) {
                if (isPermissionSetGranted(this@ReservationStatusActivity)) {
                    checkLocationSettingsAndStartService(reservationInfo.reservationId)
                } else {
                    requestRequiredPermission()
                }
            }

            override fun onCompleteClicked(reservationInfo: ReservationInfo) {
                val companionCompleteDialog =
                    CompanionCompleteDialog.newInstance(reservationInfo)

                stopLocationService()
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
                reservationStatusViewModel.changeReservation(item.reservationId, 확정)
                setObserves()
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

    private fun showNotificationPermissionDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("백그라운드 권한 설정")
            setMessage(
                String.format(
                    "백그라운드 설정을 \"항상 허용\"으로 설정해 주세요."
                )
            )
            setPositiveButton("설정하기") { _, _ ->
                requestBackgroundLocationPermission()
            }
            setNegativeButton("취소하기") { _, _ -> }
            show()
        }
    }

    private fun navigateToPrevious() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        const val REQUEST_CHECK_SETTINGS = 100
    }
}
