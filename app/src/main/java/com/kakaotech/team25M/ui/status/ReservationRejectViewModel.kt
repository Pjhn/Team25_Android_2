package com.kakaotech.team25M.ui.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaotech.team25M.data.network.dto.ReservationStatusDto
import com.kakaotech.team25M.domain.model.ReservationStatus
import com.kakaotech.team25M.domain.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationRejectViewModel @Inject constructor(
    private val reservationRepository: ReservationRepository
): ViewModel() {
    fun changeReservation(reservationId: String, status: ReservationStatus) {
        viewModelScope.launch {
            reservationRepository.changeReservation(reservationId, ReservationStatusDto(status.toString()))
        }
    }
}
