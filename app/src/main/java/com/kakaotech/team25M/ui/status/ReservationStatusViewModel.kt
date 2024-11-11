package com.kakaotech.team25M.ui.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.domain.model.Gender
import com.kakaotech.team25M.domain.model.Patient
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.model.ReservationStatus
import com.kakaotech.team25M.domain.model.ReservationStatus.*
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import com.kakaotech.team25M.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ReservationStatusViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val accompanyRepository: AccompanyRepository
) : ViewModel() {
    val confirmedOrRunningReservations = reservationRepository.getReservationsFlow()
        .map { reservations -> reservations.filter { it.reservationStatus == 확정 || it.reservationStatus == 진행중 } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
            )

    val pendingReservations = reservationRepository.getReservationsFlow()
        .map { reservations -> reservations.filter { it.reservationStatus == 보류 } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    val completedReservations = reservationRepository.getReservationsFlow()
        .map { reservations -> reservations.filter { it.reservationStatus == 완료 } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    fun changeReservation(reservationId: String, status: ReservationStatus) {
        viewModelScope.launch {
            reservationRepository.changeReservation(reservationId, status)
        }
    }


    fun postStartedAccompanyInfo(reservationId: String) {
        viewModelScope.launch {
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            accompanyRepository.postAccompanyInfo(
                reservationId,
                AccompanyDto(status = "병원 이동", statusDate = currentDateTime, statusDescribe = "동행을 시작합니다.")
            )
        }
    }

    fun postCompletedAccompanyInfo(reservationId: String) {
        viewModelScope.launch {
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            accompanyRepository.postAccompanyInfo(
                reservationId,
                AccompanyDto(status = "귀가", statusDate = currentDateTime, statusDescribe = "동행을 완료하였습니다.")
            )
        }
    }
}
