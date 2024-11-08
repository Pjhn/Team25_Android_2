package com.example.team25.ui.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.team25.data.network.dto.ReportDto
import com.example.team25.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {
    private val _reportDto = MutableStateFlow(
        ReportDto(
            doctorSummary = "",
            frequency = 0,
            medicineTime = "",
            timeOfDays = ""
        )
    )
    val reportDto: StateFlow<ReportDto> = _reportDto

    fun updateDoctorSummary(summary: String) {
        _reportDto.value = _reportDto.value.copy(doctorSummary = summary)
    }

    fun updateFrequency(frequency: Int) {
        _reportDto.value = _reportDto.value.copy(frequency = frequency)
    }

    fun updateMedicineTime(medicineTime: String) {
        _reportDto.value = _reportDto.value.copy(medicineTime = medicineTime)
    }

    fun updateTimeOfDays(timeOfDays: String) {
        _reportDto.value = _reportDto.value.copy(timeOfDays = timeOfDays)
    }

    fun postReport(reservationId: String, reportDto: ReportDto){
        viewModelScope.launch {
            reportRepository.postReport(reservationId, reportDto)
        }
    }
}
