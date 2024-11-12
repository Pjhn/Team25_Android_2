package com.kakaotech.team25M.ui.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.network.dto.ReservationStatusDto
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.model.ReservationStatus
import com.kakaotech.team25M.domain.model.ReservationStatus.*
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import com.kakaotech.team25M.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
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
    private val _reservations = MutableStateFlow<List<ReservationInfo>>(emptyList())
    val reservations: StateFlow<List<ReservationInfo>> = _reservations

    private val _confirmedOrRunningReservations = MutableStateFlow<List<ReservationInfo>>(emptyList())
    val confirmedOrRunningReservations: StateFlow<List<ReservationInfo>> = _confirmedOrRunningReservations

    private val _pendingReservations = MutableStateFlow<List<ReservationInfo>>(emptyList())
    val pendingReservations: StateFlow<List<ReservationInfo>> = _pendingReservations

    private val _completedReservations = MutableStateFlow<List<ReservationInfo>>(emptyList())
    val completedReservations: StateFlow<List<ReservationInfo>> = _completedReservations

    fun changeReservation(reservationId: String, status: ReservationStatus) {
        viewModelScope.launch {
            val result = reservationRepository.changeReservation(reservationId, ReservationStatusDto(status.toString()))
            if (result.isSuccess) {
                when (status) {
                    진행중 -> postStartedAccompanyInfo(reservationId)
                    완료 -> postCompletedAccompanyInfo(reservationId)
                    else -> {}
                }
            }
        }
    }

    fun updateReservations(){
        viewModelScope.launch {
            _reservations.value = reservationRepository.getReservationsFlow().first()
        }
    }

    fun updateConfirmedOrRunningReservations(reservations: List<ReservationInfo>){
        _confirmedOrRunningReservations.value = reservations.filter { it.reservationStatus == 진행중 || it.reservationStatus == 확정}
    }
    fun updatePendingReservations(reservations: List<ReservationInfo>){
        _pendingReservations.value = reservations.filter { it.reservationStatus == 보류 }
    }
    fun updateCompletedReservations(reservations: List<ReservationInfo>){
        _completedReservations.value = reservations.filter { it.reservationStatus == 완료 }
    }

    fun postStartedAccompanyInfo(reservationId: String) {
        viewModelScope.launch {
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            accompanyRepository.postAccompanyInfo(
                reservationId,
                AccompanyDto(status = "병원 이동", statusDate = currentDateTime, statusDescribe = "동행을 시작합니다.")
            )
        }
    }

    fun postCompletedAccompanyInfo(reservationId: String) {
        viewModelScope.launch {
            val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            accompanyRepository.postAccompanyInfo(
                reservationId,
                AccompanyDto(status = "귀가", statusDate = currentDateTime, statusDescribe = "동행을 완료하였습니다.")
            )
        }
    }
}
