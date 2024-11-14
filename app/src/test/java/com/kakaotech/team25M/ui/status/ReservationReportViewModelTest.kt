package com.kakaotech.team25M.ui.status

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kakaotech.team25M.data.network.dto.ReportDto
import com.kakaotech.team25M.domain.repository.ReportRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReservationReportViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ReservationReportViewModel
    private val reportRepository: ReportRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ReservationReportViewModel(reportRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `DoctorSummary 업데이트 시 doctorSummary 값이 변경된다`() = runTest {
        // Given
        val summary = "환자 상태가 양호합니다."

        // When
        viewModel.updateDoctorSummary(summary)

        // Then
        assertEquals(summary, viewModel.reportDto.first().doctorSummary)
    }

    @Test
    fun `Frequency 업데이트 시 frequency 값이 변경된다`() = runTest {
        // Given
        val frequency = 3

        // When
        viewModel.updateFrequency(frequency)

        // Then
        assertEquals(frequency, viewModel.reportDto.first().frequency)
    }

    @Test
    fun `MedicineTime 업데이트 시 medicineTime 값이 변경된다`() = runTest {
        // Given
        val medicineTime = "아침, 저녁"

        // When
        viewModel.updateMedicineTime(medicineTime)

        // Then
        assertEquals(medicineTime, viewModel.reportDto.first().medicineTime)
    }

    @Test
    fun `TimeOfDays 업데이트 시 timeOfDays 값이 변경된다`() = runTest {
        // Given
        val timeOfDays = "주 3회"

        // When
        viewModel.updateTimeOfDays(timeOfDays)

        // Then
        assertEquals(timeOfDays, viewModel.reportDto.first().timeOfDays)
    }

    @Test
    fun `postReport 호출 시 repository의 postReport 메서드가 호출된다`() = runTest {
        // Given
        val reservationId = "1"
        val reportDto = ReportDto(
            doctorSummary = "환자 상태가 양호합니다.",
            frequency = 3,
            medicineTime = "AFTER_MEAL",
            timeOfDays = "아침 저녁"
        )

        coEvery { reportRepository.postReport(reservationId, reportDto) } returns Unit

        // When
        viewModel.postReport(reservationId, reportDto)

        // Then
        coVerify { reportRepository.postReport(reservationId, reportDto) }
    }
}
