package com.kakaotech.team25M.ui.companion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaotech.team25M.domain.model.ReservationStatus
import com.kakaotech.team25M.domain.model.ReservationStatus.진행중
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import com.kakaotech.team25M.domain.repository.ReservationRepository
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.network.dto.ReservationStatusDto
import com.kakaotech.team25M.domain.model.AccompanyInfo
import com.kakaotech.team25M.domain.model.ReservationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveCompanionViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository,
    private val accompanyRepository: AccompanyRepository
) : ViewModel() {
    private val _runningReservation = MutableStateFlow<ReservationInfo?>(null)
    val runningReservation: StateFlow<ReservationInfo?> = _runningReservation

    private val _accompanyInfo = MutableStateFlow<List<AccompanyInfo>?>(null)
    val accompanyInfo: StateFlow<List<AccompanyInfo>?> = _accompanyInfo

    var reservationId: String? = null

    init {
        getFilteredRunningReservation()
    }

    fun getFilteredRunningReservation() {
        viewModelScope.launch {
            _runningReservation.value =
                reservationRepository.getReservationsFlow().firstOrNull()
                    ?.firstOrNull { it.reservationStatus == 진행중 }
        }
    }

    fun changeReservation(reservationId: String, status: ReservationStatus) {
        viewModelScope.launch {
            reservationRepository.changeReservation(reservationId, ReservationStatusDto(status.toString()))
        }
    }

    fun postAccompanyInfo(reservationId: String, accompanyDto: AccompanyDto) {
        viewModelScope.launch {
            accompanyRepository.postAccompanyInfo(reservationId, accompanyDto)
            updateAccompanyInfo(reservationId)
        }
    }

    fun updateAccompanyInfo(reservationId: String) {
        viewModelScope.launch {
            _accompanyInfo.value = accompanyRepository.getAccompanyFlow(reservationId).firstOrNull()
        }
    }
}
