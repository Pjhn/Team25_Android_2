package com.kakaotech.team25M.ui.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.network.dto.CoordinatesDto
import com.kakaotech.team25M.domain.model.Gender
import com.kakaotech.team25M.domain.model.Patient
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.model.ReservationStatus
import com.kakaotech.team25M.domain.model.ReservationStatus.*
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import com.kakaotech.team25M.domain.repository.CoordinatesRepository
import com.kakaotech.team25M.domain.repository.ReservationRepository
import com.kakaotech.team25M.ui.status.utils.LocationManager
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
    private val locationManager: LocationManager,
    private val reservationRepository: ReservationRepository,
    private val coordinatesRepository: CoordinatesRepository,
    private val accompanyRepository: AccompanyRepository
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
            initialValue = listOf(
                ReservationInfo(
                    managerId = "19",
                    reservationId = "initial_confirmed",
                    reservationStatus = 확정,
                    departureLocation = "Initial Departure",
                    arrivalLocation = "Initial Arrival",
                    reservationDate = "2024-01-01T00:00:00",
                    serviceType = "Initial Service",
                    transportation = "Initial Transport",
                    price = 0,
                    patient = Patient(
                        patientName = "John Doe",
                        patientPhone = "010-1234-5678",
                        patientGender = Gender.MALE,
                        patientRelation = "Father",
                        patientBirth = "1955-05-10",
                        nokPhone = "010-8765-4321"
                    )
                )
            )
        )

    val pendingReservations = reservationRepository.getReservationsFlow()
        .map { reservations -> reservations.filter { it.reservationStatus == 보류 } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = listOf(
                ReservationInfo(
                    managerId = "19",
                    reservationId = "initial_pending",
                    reservationStatus = 보류,
                    departureLocation = "Initial Departure",
                    arrivalLocation = "Initial Arrival",
                    reservationDate = "2024-01-01T00:00:00",
                    serviceType = "Initial Service",
                    transportation = "Initial Transport",
                    price = 0,
                    patient = Patient(
                        patientName = "John Doe",
                        patientPhone = "010-1234-5678",
                        patientGender = Gender.MALE,
                        patientRelation = "Father",
                        patientBirth = "1955-05-10",
                        nokPhone = "010-8765-4321"
                    )
                )
            )
        )

    val completedReservations = reservationRepository.getReservationsFlow()
        .map { reservations -> reservations.filter { it.reservationStatus == 완료 } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = listOf(
                ReservationInfo(
                    managerId = "19",
                    reservationId = "initial_completed",
                    reservationStatus = 완료,
                    departureLocation = "Initial Departure",
                    arrivalLocation = "Initial Arrival",
                    reservationDate = "2024-01-01T00:00:00",
                    serviceType = "Initial Service",
                    transportation = "Initial Transport",
                    price = 0,
                    patient = Patient(
                        patientName = "John Doe",
                        patientPhone = "010-1234-5678",
                        patientGender = Gender.MALE,
                        patientRelation = "Father",
                        patientBirth = "1955-05-10",
                        nokPhone = "010-8765-4321"
                    )
                )
            )
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
