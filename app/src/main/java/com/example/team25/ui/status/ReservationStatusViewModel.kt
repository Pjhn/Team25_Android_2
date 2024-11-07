package com.example.team25.ui.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.team25.data.network.dto.CoordinatesDto
import com.example.team25.domain.model.ReservationStatus
import com.example.team25.domain.model.ReservationStatus.*
import com.example.team25.domain.repository.CoordinatesRepository
import com.example.team25.domain.repository.ReservationRepository
import com.example.team25.ui.status.utils.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationStatusViewModel @Inject constructor(
    private val locationManager: LocationManager,
    private val reservationRepository: ReservationRepository,
    private val coordinatesRepository: CoordinatesRepository
) : ViewModel() {
    val latitudeFlow = locationManager.latitudeFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)
    val longitudeFlow = locationManager.longitudeFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    private val _runningReservationId = MutableStateFlow<String>("")
    val runningReservationId: StateFlow<String> = _runningReservationId

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

    init {
        postCoordinates()
    }

    fun changeReservation(reservationId: String, status: ReservationStatus) {
        viewModelScope.launch {
            reservationRepository.changeReservation(reservationId, status)
        }
    }

    fun updateRunningReservationId(id: String) {
        viewModelScope.launch {
            _runningReservationId.value = id
        }
    }

    private fun postCoordinates() {
        viewModelScope.launch {
            combine(latitudeFlow, longitudeFlow) { latitude, longitude ->
                Pair(latitude ?: 0.0, longitude ?: 0.0)
            }.collectLatest { (latitude, longitude) ->
                coordinatesRepository.postCoordinates(runningReservationId.value, CoordinatesDto(latitude, longitude))
            }
        }
    }
}
