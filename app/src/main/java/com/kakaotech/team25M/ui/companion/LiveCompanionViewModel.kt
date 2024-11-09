package com.kakaotech.team25M.ui.companion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaotech.team25M.domain.model.ReservationStatus
import com.kakaotech.team25M.domain.model.ReservationStatus.진행중
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import com.kakaotech.team25M.domain.repository.CoordinatesRepository
import com.kakaotech.team25M.domain.repository.ReservationRepository
import com.kakao.vectormap.LatLng
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.domain.model.AccompanyInfo
import com.kakaotech.team25M.domain.model.ReservationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveCompanionViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val coordinatesRepository: CoordinatesRepository,
    private val accompanyRepository: AccompanyRepository
) : ViewModel() {
    private val _reservationId = MutableStateFlow<String>("")
    val reservationId: StateFlow<String> = _reservationId

    val runningReservation = reservationRepository.getReservationsFlow()
        .map { reservations ->
            reservations.first { it.reservationStatus == 진행중 }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ReservationInfo()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val accompanyInfoList: StateFlow<List<AccompanyInfo>> = _reservationId
        .flatMapLatest { reservationId ->
            accompanyRepository.getAccompanyFlow(reservationId)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val coordinatesInfo: StateFlow<LatLng> = _reservationId
        .flatMapLatest { reservationId ->
            pollingFlow(5_000L) {
                coordinatesRepository.getCoordinatesFlow(reservationId)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LatLng.from(35.55, 128.0)
        )

    init {
       viewModelScope.launch {
            runningReservation.collectLatest { reservation ->
                _reservationId.value = reservation.reservationId
            }
        }
    }

    fun changeReservation(reservationId: String, status: ReservationStatus) {
        viewModelScope.launch {
            reservationRepository.changeReservation(reservationId,status)
        }
    }

    fun postAccompanyInfo(reservationId: String, accompanyDto: AccompanyDto) {
        viewModelScope.launch {
            accompanyRepository.postAccompanyInfo(reservationId, accompanyDto)
        }
    }

    private fun <T> pollingFlow(intervalMillis: Long = 5_000L, function: () -> Flow<T>): Flow<T> = flow {
        while (true) {
            function().collect { value ->
                emit(value)
            }
            delay(intervalMillis)
        }
    }
}