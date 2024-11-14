package com.kakaotech.team25M.ui.status

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.domain.model.ReservationInfo
import com.kakaotech.team25M.domain.model.ReservationStatus
import com.kakaotech.team25M.domain.repository.AccompanyRepository
import com.kakaotech.team25M.domain.repository.ReservationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReservationStatusViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ReservationStatusViewModel
    private val reservationRepository: ReservationRepository = mockk(relaxed = true)
    private val accompanyRepository: AccompanyRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = ReservationStatusViewModel(reservationRepository, accompanyRepository)
        mockLogClass()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `진행중 상태로 변경 시 postStartedAccompanyInfo 호출`() = runTest {
        // Given
        val reservationId = "1"
        val mockReservations = listOf(
            ReservationInfo(
                reservationId = reservationId,
                reservationStatus = ReservationStatus.진행중
            )
        )
        coEvery { reservationRepository.changeReservation(reservationId, any()) } returns Result.success("")
        coEvery { reservationRepository.getReservationsFlow() } returns flowOf(mockReservations)

        // When
        viewModel.changeReservation(reservationId, ReservationStatus.진행중)

        // Then
        coVerify { accompanyRepository.postAccompanyInfo(reservationId, any()) }
    }

    @Test
    fun `완료 상태로 변경 시 postCompletedAccompanyInfo 호출`() = runTest {
        // Given
        val reservationId = "2"
        val mockReservations = listOf(
            ReservationInfo(
                reservationId = reservationId,
                reservationStatus = ReservationStatus.완료
            )
        )
        coEvery { reservationRepository.changeReservation(reservationId, any()) } returns Result.success("")
        coEvery { reservationRepository.getReservationsFlow() } returns flowOf(mockReservations)

        // When
        viewModel.changeReservation(reservationId, ReservationStatus.완료)

        // Then
        coVerify { accompanyRepository.postAccompanyInfo(any(), any()) }
    }

    @Test
    fun `진행중 또는 확정 상태만 필터링`() = runTest {
        // Given
        val reservations = listOf(
            ReservationInfo(reservationStatus = ReservationStatus.진행중),
            ReservationInfo(reservationStatus = ReservationStatus.확정),
            ReservationInfo(reservationStatus = ReservationStatus.보류)
        )

        // When
        viewModel.updateConfirmedOrRunningReservations(reservations)

        // Then
        val result = viewModel.confirmedOrRunningReservations.first()
        assertEquals(2, result.size)
        assert(result.all { it.reservationStatus == ReservationStatus.진행중 || it.reservationStatus == ReservationStatus.확정 })
    }

    @Test
    fun `보류 상태만 필터링`() = runTest {
        // Given
        val reservations = listOf(
            ReservationInfo(reservationStatus = ReservationStatus.보류),
            ReservationInfo(reservationStatus = ReservationStatus.확정)
        )

        // When
        viewModel.updatePendingReservations(reservations)

        // Then
        val result = viewModel.pendingReservations.first()
        assertEquals(1, result.size)
        assertEquals(ReservationStatus.보류, result.first().reservationStatus)
    }

    @Test
    fun `완료 상태만 필터링`() = runTest {
        // Given
        val reservations = listOf(
            ReservationInfo(reservationStatus = ReservationStatus.완료),
            ReservationInfo(reservationStatus = ReservationStatus.확정)
        )

        // When
        viewModel.updateCompletedReservations(reservations)

        // Then
        val result = viewModel.completedReservations.first()
        assertEquals(1, result.size)
        assertEquals(ReservationStatus.완료, result.first().reservationStatus)
    }

    @Test
    fun `postStartedAccompanyInfo 호출 시 accompanyRepository의 postAccompanyInfo 호출`() = runTest {
        // Given
        val reservationId = "3"

        // When
        viewModel.postStartedAccompanyInfo(reservationId)

        // Then
        coVerify {
            accompanyRepository.postAccompanyInfo(
                any(),
                match { dto ->
                    dto.status == "병원 이동" &&
                        dto.statusDescribe == "동행을 시작합니다." &&
                        dto.statusDate.isNotEmpty()
                }
            )
        }
    }

    @Test
    fun `postCompletedAccompanyInfo 호출 시 accompanyRepository의 postAccompanyInfo 호출`() = runTest {
        // Given
        val reservationId = "4"

        // When
        viewModel.postCompletedAccompanyInfo(reservationId)

        // Then
        coVerify {
            accompanyRepository.postAccompanyInfo(
                any(),
                match { dto ->
                    dto.status == "귀가" &&
                        dto.statusDescribe == "동행을 완료하였습니다." &&
                        dto.statusDate.isNotEmpty()
                }
            )
        }
    }

    private fun mockLogClass() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }
}
