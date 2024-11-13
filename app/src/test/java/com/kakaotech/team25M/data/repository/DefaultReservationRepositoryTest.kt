package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.Patient
import com.kakaotech.team25M.data.network.dto.ReservationDto
import com.kakaotech.team25M.data.network.dto.ReservationStatusDto
import com.kakaotech.team25M.data.network.dto.ServiceResponse
import com.kakaotech.team25M.data.network.dto.mapper.asDomain
import com.kakaotech.team25M.data.network.services.ReservationService
import com.kakaotech.team25M.domain.model.ReservationStatus
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class DefaultReservationRepositoryTest {

    private lateinit var reservationService: ReservationService
    private lateinit var reservationRepository: DefaultReservationRepository

    @Before
    fun setUp() {
        reservationService = mockk()
        reservationRepository = DefaultReservationRepository(reservationService)
        mockLogClass()
    }

    @Test
    fun `예약 목록 불러오기 성공 시 예약 목록 정보 방출`() = runTest {
        // Given
        val reservationsDto = listOf(
            ReservationDto(
                reservationId = "1",
                managerId = "1",
                departureLocation = "서울",
                arrivalLocation = "부산",
                reservationDate = "2024-09-01T15:32:00",
                serviceType = "정기동행",
                transportation = "버스",
                reservationStatus = ReservationStatus.보류,
                price = 20000,
                patient = Patient(
                    name = "최민욱",
                    phoneNumber = "01012345678",
                    patientGender = "남성",
                    patientRelation = "본인",
                    birthDate = "1974-04-15",
                    nokPhone = "01012344321"
                )
            )
        )

        val response = ServiceResponse(data = reservationsDto, message = "성공", status = true)

        coEvery { reservationService.getReservations() } returns Response.success(response)

        // When
        val result = reservationRepository.getReservationsFlow().firstOrNull()

        // Then
        assertEquals(reservationsDto.asDomain(), result)
        coVerify { reservationService.getReservations() }
        verify { Log.d("ReservationRepository", response.toString()) }
    }

    @Test
    fun `예약 목록 불러오기 실패 시 로그 출력`() = runBlocking {
        // Given
        coEvery { reservationService.getReservations() } returns Response.error(
            404,
            "존재하지 않는 예약입니다".toResponseBody()
        )

        // When
        val result = reservationRepository.getReservationsFlow().firstOrNull()

        // Then
        assertEquals(null, result)
        coVerify { reservationService.getReservations() }
        verify { Log.e("ReservationRepository", "404") }
    }

    @Test
    fun `예약 상태 변경 성공 시 성공 결과 반환`() = runTest {
        // Given
        val reservationId = "2"
        val reservationStatusDto = ReservationStatusDto(
            reservationStatus = "진행중"
        )
        val reservationDto = ReservationDto(
            reservationId = "2",
            managerId = "2",
            departureLocation = "울산",
            arrivalLocation = "부산",
            reservationDate = "2024-09-01T15:32:00",
            serviceType = "정기동행",
            transportation = "버스",
            reservationStatus = ReservationStatus.보류,
            price = 20000,
            patient = Patient(
                name = "여상욱",
                phoneNumber = "01012345678",
                patientGender = "남성",
                patientRelation = "본인",
                birthDate = "1974-04-15",
                nokPhone = "01012344321"
            )
        )
        val response = ServiceResponse(data = reservationDto, message = "변경 성공", status = true)

        coEvery { reservationService.changeReservation(reservationId, reservationStatusDto) } returns Response.success(response)

        // When
        val result = reservationRepository.changeReservation(reservationId, reservationStatusDto)

        // Then
        assert(result.isSuccess)
        assertEquals("변경 성공", result.getOrNull())
        coVerify { reservationService.changeReservation(reservationId, reservationStatusDto) }
        verify { Log.d("ReservationRepository", response.toString()) }
    }

    @Test
    fun `예약 상태 변경 실패 시 실패 결과 반환`() = runBlocking {
        // Given
        val reservationId = "2"
        val reservationStatusDto = ReservationStatusDto(
            reservationStatus = "완료"
        )

        coEvery { reservationService.changeReservation(reservationId, reservationStatusDto) } returns Response.error(
            400,
            "예약 변경 실패".toResponseBody()
        )

        // When
        val result = reservationRepository.changeReservation(reservationId, reservationStatusDto)

        // Then
        assert(result.isFailure)
        coVerify { reservationService.changeReservation(reservationId, reservationStatusDto) }
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
