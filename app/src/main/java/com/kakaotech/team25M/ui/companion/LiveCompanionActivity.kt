package com.kakaotech.team25M.ui.companion

import android.content.Intent
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
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakaotech.team25M.R
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.network.services.LocationUpdateService
import com.kakaotech.team25M.databinding.ActivityLiveCompanionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class LiveCompanionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveCompanionBinding
    private val kakaoMapDeferred = CompletableDeferred<KakaoMap>()
    private val liveCompanionViewModel: LiveCompanionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLiveCompanionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startMapView()
        setupObserves()
        setLiveCompanionRecyclerViewAdapter()
        setClickListener()
        navigateToPrevious()
    }

    private fun startMapView() {
        binding.mapView.start(
            createMapLifeCycleCallback(),
            createMapReadyCallback(),
        )
    }

    private fun createMapLifeCycleCallback(): MapLifeCycleCallback {
        return object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}

            override fun onMapError(error: Exception?) {}
        }
    }

    private fun createMapReadyCallback(): KakaoMapReadyCallback {
        return object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                kakaoMapDeferred.complete(kakaoMap)
            }
        }
    }

    private fun updateLabelsToMap(labelManager: LabelManager, latLng: LatLng) {
        val styles =
            LabelStyles.from(
                LabelStyle.from(R.drawable.marker).setZoomLevel(8),
            )

        val labelOptions =
            LabelOptions.from(
                LatLng.from(latLng),
            )
                .setStyles(styles)

        labelManager.layer?.removeAll()
        labelManager.layer?.addLabel(labelOptions)
    }

    private fun updateMapLocation(kakaoMap: KakaoMap, latLng: LatLng) {
        val labelManager = kakaoMap.labelManager

        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(latLng))

        if (labelManager != null) updateLabelsToMap(labelManager, latLng)
    }

    private fun setupObserves() {
        collectRunningReservation()
        collectCoordinates()
        collectAccompanyInfoList()
    }

    private fun collectRunningReservation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                liveCompanionViewModel.runningReservation.collectLatest { reservationInfo ->
                    binding.patientNameTextView.text = reservationInfo.patient.patientName
                }
            }
        }
    }

    private fun collectCoordinates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val kakaoMap = kakaoMapDeferred.await()

                liveCompanionViewModel.coordinatesInfo.collectLatest { latlng ->
                    updateMapLocation(kakaoMap, latlng)
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
                        accompanyInfoList.map { dateFormat.format(it.statusDate) + " " + it.statusDescribe }
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

            liveCompanionViewModel.postAccompanyInfo(
                liveCompanionViewModel.reservationId.value,
                AccompanyDto("병원 이동", statusDate = currentDate, statusDescribe = statusDescribe)
            )

            collectAccompanyInfoList()
            binding.companionStatusInputEditText.text.clear()
        }
    }

    private fun setCompanionCompleteBtnClickListener() {
        binding.completeCompanionBtn.setOnClickListener {
            val companionCompleteDialog =
                CompanionCompleteDialog.newInstance(liveCompanionViewModel.runningReservation.value)

            stopLocationService()
            liveCompanionViewModel.changeReservation(liveCompanionViewModel.reservationId.value, 완료)
            companionCompleteDialog.show(supportFragmentManager, "CompanionCompleteDialog")
        }
    }

    private fun stopLocationService() {
        val intent = Intent(this, LocationUpdateService::class.java)
        this.stopService(intent)
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

    @Override
    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    @Override
    public override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }
}