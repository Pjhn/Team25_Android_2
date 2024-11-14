package com.kakaotech.team25M.data.repository

import android.util.Log
import com.kakaotech.team25M.data.network.dto.AccompanyDto
import com.kakaotech.team25M.data.network.dto.ServiceResponse
import com.kakaotech.team25M.data.network.dto.mapper.asDomain
import com.kakaotech.team25M.data.network.services.AccompanyService
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
import org.mockito.ArgumentMatchers.contains
import retrofit2.Response

@ExperimentalCoroutinesApi
class DefaultAccompanyRepositoryTest {

    private lateinit var accompanyService: AccompanyService
    private lateinit var accompanyRepository: DefaultAccompanyRepository

    @Before
    fun setUp() {
        accompanyService = mockk()
        accompanyRepository = DefaultAccompanyRepository(accompanyService)
        mockLogClass()
    }

    @Test
    fun `동행 정보 전송 성공 시 성공 결과 반환`() = runTest {
        // Given
        val reservationId = "1"
        val accompanyDto = AccompanyDto(
            status = "병원 이동",
            statusDate = "2024-09-01T15:32:00",
            statusDescribe = "병원으로 이동중입니다."
        )
        val response = ServiceResponse(data = accompanyDto, message = "성공", status = true)

        coEvery { accompanyService.postAccompanyInfo(reservationId, accompanyDto) } returns Response.success(response)

        // When
        accompanyRepository.postAccompanyInfo(reservationId, accompanyDto)

        // Then
        coVerify { accompanyService.postAccompanyInfo(reservationId, accompanyDto) }
        verify { Log.d("AccompanyRepository", response.toString()) }
    }

    @Test
    fun `동행 정보 전송 실패 시 로그 출력`() = runTest {
        // Given
        val reservationId = "2"
        val accompanyDto = AccompanyDto(
            status = "진료 접수",
            statusDate = "2024-09-01T15:32:00",
            statusDescribe = "진료 접수 중입니다."
        )

        coEvery { accompanyService.postAccompanyInfo(reservationId, accompanyDto) } returns Response.error(
            400,
            "등록되지 않은 회원입니다".toResponseBody()
        )

        // When
       accompanyRepository.postAccompanyInfo(reservationId, accompanyDto)

        // Then
        coVerify { accompanyService.postAccompanyInfo(reservationId, accompanyDto) }
        verify { Log.e("AccompanyRepository", "400")}

    }

    @Test
    fun `동행 정보 전송 성공 시 동행 정보 리스트 방출`() = runTest {
        // Given
        val reservationId = "3"
        val accompanyDtos = listOf(
            AccompanyDto(
                status = "검사 및 진료",
                statusDate = "2024-09-01T15:32:00",
                statusDescribe = "검사 및 진료 중입니다."
            )
        )
        val response = ServiceResponse(data = accompanyDtos, message = "성공", status = true)

        coEvery { accompanyService.getAccompanyInfo(reservationId) } returns Response.success(response)

        // When
        val result = accompanyRepository.getAccompanyFlow(reservationId).firstOrNull()

        // Then
        assertEquals(accompanyDtos.asDomain(), result)
        coVerify { accompanyService.getAccompanyInfo(reservationId) }
    }

    @Test
    fun `동행 정보 조회 실패 시 null 방출`() = runBlocking {
        // Given
        val reservationId = "4"

        coEvery { accompanyService.getAccompanyInfo(reservationId) } returns Response.error(
            404,
            "존재하지 예약입니다.".toResponseBody()
        )

        // When
        val result = accompanyRepository.getAccompanyFlow(reservationId).firstOrNull()

        // Then
        assertEquals(null, result)
        coVerify { accompanyService.getAccompanyInfo(reservationId) }
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
