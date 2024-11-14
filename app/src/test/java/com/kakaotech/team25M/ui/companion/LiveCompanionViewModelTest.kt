package com.kakaotech.team25M.ui.companion

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kakaotech.team25M.domain.model.*
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.network.dto.ReservationStatusDto
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import com.kakaotech.team25M.domain.repository.ReservationRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LiveCompanionViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LiveCompanionViewModel
    private val reservationRepository: ReservationRepository = mockk()
    private val accompanyRepository: AccompanyRepository = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LiveCompanionViewModel(reservationRepository, accompanyRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기 로드 시 진행중 예약 필터링`() = runBlocking {
        // Given
        val reservationList = listOf(
            ReservationInfo(reservationId = "1", reservationStatus = ReservationStatus.보류),
            ReservationInfo(reservationId = "2", reservationStatus = ReservationStatus.진행중),
            ReservationInfo(reservationId = "3", reservationStatus = ReservationStatus.완료)
        )
        coEvery { reservationRepository.getReservationsFlow() } returns flowOf(reservationList)

        // When
        viewModel.getFilteredRunningReservation()

        // Then
        assertEquals("2", viewModel.runningReservation.value?.reservationId)
    }

    @Test
    fun `동행 정보 등록 후 갱신`() = runBlocking {
        // Given
        val reservationId = "1"
        val accompanyDto = AccompanyDto(
            status = "동행 시작",
            statusDate = "2024-11-12T10:00:00",
            statusDescribe = "동행 시작합니다."
        )
        val updatedAccompanyInfo = listOf(
            AccompanyInfo(status = "동행 시작", statusDate = "2024-11-12T10:00:00", statusDescribe = "동행 시작합니다.")
        )

        coEvery { accompanyRepository.postAccompanyInfo(reservationId, accompanyDto) } just Runs
        coEvery { accompanyRepository.getAccompanyFlow(reservationId) } returns flowOf(updatedAccompanyInfo)

        // When
        viewModel.postAccompanyInfo(reservationId, accompanyDto)

        // Then
        assertEquals(updatedAccompanyInfo, viewModel.accompanyInfo.value)
        coVerify { accompanyRepository.postAccompanyInfo(reservationId, accompanyDto) }
        coVerify { accompanyRepository.getAccompanyFlow(reservationId) }
    }
}
